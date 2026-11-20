package com.blog.daoimpl;

import java.util.List;
import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.blog.dao.CommentDao;
import com.blog.pojo.Comment;

@Component
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private HibernateTemplate hTemplate;

    public void sethTemplate(HibernateTemplate hTemplate) {
        this.hTemplate = hTemplate;
    }

    @Override
    @Transactional
    public boolean addComment(Comment c) {
        try {
            hTemplate.save(c);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateComment(Comment c) {
        try {
            hTemplate.update(c);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteComment(Comment c) {
        try {
            hTemplate.delete(c);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    
    
    
    @Override
    @Transactional
    public List<Comment> getCommentByPostId(int pId) {
        try {
            String hql = "select c from Comment c join fetch c.user where c.post.pId = :pid order by c.id desc";
            @SuppressWarnings("unchecked")
            List<Comment> lst = (List<Comment>) hTemplate.findByNamedParam(hql, "pid", pId);
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public List<Comment> getCommentsForPosts(List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) return new ArrayList<>();
        try {
            return hTemplate.execute(session -> {
                org.hibernate.query.Query<Comment> query = session.createQuery(
                        "select c from Comment c join fetch c.user where c.post.pId in (:postIds) order by c.id desc",
                        Comment.class);
                query.setParameterList("postIds", postIds);
                return query.list();
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    
    
    
    
    @Override
    @Transactional
    public List<Comment> getCommentByUserId(int id) {
        try {
            String hql = "select c from Comment c join fetch c.post join fetch c.user where c.user.id = :uid order by c.id desc";
            @SuppressWarnings("unchecked")
            List<Comment> lst = (List<Comment>) hTemplate.findByNamedParam(hql, "uid", id);
            return lst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
