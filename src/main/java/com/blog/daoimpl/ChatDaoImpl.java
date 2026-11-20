package com.blog.daoimpl;

import java.util.Collections;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.blog.dao.ChatDao;
import com.blog.pojo.ChatMessage;

@Component
public class ChatDaoImpl implements ChatDao {
    @Autowired private HibernateTemplate hTemplate;

    @Override @Transactional(readOnly = true)
    public ChatMessage findByClientToken(int senderId, String token) {
        if (token == null || token.trim().isEmpty()) return null;
        return hTemplate.execute(session -> {
            Query<ChatMessage> q = session.createQuery("from ChatMessage m where m.sender.id=:sender and m.clientToken=:token", ChatMessage.class);
            q.setParameter("sender", senderId); q.setParameter("token", token); return q.uniqueResult();
        });
    }

    @Override @Transactional
    public ChatMessage save(ChatMessage message) { hTemplate.save(message); hTemplate.flush(); return message; }

    @Override @Transactional(readOnly = true)
    public List<ChatMessage> getConversation(int a, int b) {
        List<ChatMessage> rows = hTemplate.execute(session -> {
            Query<ChatMessage> q = session.createQuery("select m from ChatMessage m join fetch m.sender join fetch m.receiver where (m.sender.id=:a and m.receiver.id=:b) or (m.sender.id=:b and m.receiver.id=:a) order by m.createdAt desc, m.id desc", ChatMessage.class);
            q.setParameter("a", a); q.setParameter("b", b); q.setMaxResults(500); return q.list();
        });
        if (rows == null) return Collections.emptyList();
        Collections.reverse(rows);
        return rows;
    }

    @Override @Transactional(readOnly = true)
    public List<ChatMessage> getRecentMessages(int userId) {
        List<ChatMessage> rows = hTemplate.execute(session -> {
            Query<ChatMessage> q = session.createQuery("select m from ChatMessage m join fetch m.sender join fetch m.receiver where m.sender.id=:id or m.receiver.id=:id order by m.createdAt desc, m.id desc", ChatMessage.class);
            q.setParameter("id", userId); q.setMaxResults(1000); return q.list();
        });
        return rows == null ? Collections.emptyList() : rows;
    }

    @Override @Transactional(readOnly = true)
    public long getUnreadCount(int receiverId, int senderId) {
        Long count = hTemplate.execute(session -> {
            Query<Long> q = session.createQuery("select count(m.id) from ChatMessage m where m.receiver.id=:receiver and m.sender.id=:sender and m.seen=false", Long.class);
            q.setParameter("receiver", receiverId); q.setParameter("sender", senderId); return q.uniqueResult();
        });
        return count == null ? 0 : count;
    }

    @Override @Transactional
    public int markConversationSeen(int receiverId, int senderId) {
        return hTemplate.execute(session -> {
            Query<?> q = session.createQuery("update ChatMessage m set m.seen=true where m.receiver.id=:receiver and m.sender.id=:sender and m.seen=false");
            q.setParameter("receiver", receiverId); q.setParameter("sender", senderId); return q.executeUpdate();
        });
    }

    @Override @Transactional(readOnly = true)
    public ChatMessage getMessage(int id) { return hTemplate.get(ChatMessage.class, id); }
}
