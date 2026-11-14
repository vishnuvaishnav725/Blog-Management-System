package com.blog.daoimpl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.blog.pojo.Likes;

@Component
public class LikesDaoImpl {

    @Autowired
    private HibernateTemplate hTemplate;

    public void sethTemplate(HibernateTemplate hTemplate) {
        this.hTemplate = hTemplate;
    }

    @Transactional
    public boolean addLike(int pId, int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId in addLike: " + userId);
        }
        try {
           
            String checkHql = "from com.blog.pojo.Likes where pId = :p and userId = :u";
            @SuppressWarnings("unchecked")
            List<Likes> existing = (List<Likes>) hTemplate.findByNamedParam(
                    checkHql,
                    new String[] { "p", "u" },
                    new Object[] { pId, userId }
            );
            if (existing != null && !existing.isEmpty()) {
                System.out.println("addLike: already exists for user=" + userId + " post=" + pId);
                return true;
            }

           
            Likes like = new Likes();
            like.setpId(pId);
            like.setUserId(userId);
            hTemplate.save(like);
            hTemplate.flush();
            System.out.println("addLike: saved Likes row for user=" + userId + " post=" + pId);

           
            Integer updated = hTemplate.execute(session -> {
                Query<?> q = session.createQuery(
                        "update com.blog.pojo.Post set likeCount = coalesce(likeCount, 0) + 1 where pId = :p"
                );
                q.setParameter("p", pId);
                int u = q.executeUpdate();
             
                return u;
            });
            System.out.println("addLike: post update rows = " + updated);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean removeLike(int pId, int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId in removeLike: " + userId);
        }
        try {
            
            Integer deleted = hTemplate.execute(session -> {
                Query<?> dq = session.createQuery("delete from com.blog.pojo.Likes where pId = :p and userId = :u");
                dq.setParameter("p", pId);
                dq.setParameter("u", userId);
                return dq.executeUpdate();
            });

            System.out.println("removeLike: delete rows = " + deleted + " for user=" + userId + " post=" + pId);

           
            if (deleted != null && deleted > 0) {
                Integer updated = hTemplate.execute(session -> {
                    Query<?> uq = session.createQuery(
                        "update com.blog.pojo.Post " +
                        "set likeCount = coalesce(likeCount, 0) - 1 " +
                        "where pId = :p and coalesce(likeCount, 0) > 0"
                    );
                    uq.setParameter("p", pId);
                    return uq.executeUpdate();
                });
                System.out.println("removeLike: post update rows = " + updated);
                return true;
            } else {
                System.out.println("removeLike: nothing deleted -> skipping decrement");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean isLikedByUser(int pId, int userId) {
        if (userId <= 0) return false;
        try {
            String hql = "from com.blog.pojo.Likes where pId = :p and userId = :u";
            @SuppressWarnings("unchecked")
            List<Likes> lst = (List<Likes>) hTemplate.findByNamedParam(
                    hql,
                    new String[] { "p", "u" },
                    new Object[] { pId, userId }
            );
            return lst != null && !lst.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public int countLikes(int pId) {
        try {
            String hql = "select count(*) from com.blog.pojo.Likes where pId = :p";
            @SuppressWarnings("unchecked")
            List<Long> lst = (List<Long>) hTemplate.findByNamedParam(hql, "p", pId);
            if (lst != null && !lst.isEmpty()) {
                return lst.get(0).intValue();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public Set<Integer> getLikedPostIds(int userId, List<Integer> postIds) {
        if (userId <= 0 || postIds == null || postIds.isEmpty()) return new HashSet<>();
        try {
            List<Integer> ids = hTemplate.execute(session -> {
                Query<Integer> query = session.createQuery(
                        "select l.pId from Likes l where l.userId = :uid and l.pId in (:postIds)", Integer.class);
                query.setParameter("uid", userId);
                query.setParameterList("postIds", postIds);
                return query.list();
            });
            return ids == null ? new HashSet<>() : new HashSet<>(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Transactional
    public Map<Integer, Integer> getLikeCounts(List<Integer> postIds) {
        Map<Integer, Integer> counts = new HashMap<>();
        if (postIds == null || postIds.isEmpty()) return counts;
        try {
            List<Object[]> rows = hTemplate.execute(session -> {
                Query<Object[]> query = session.createQuery(
                        "select l.pId, count(l.id) from Likes l where l.pId in (:postIds) group by l.pId",
                        Object[].class);
                query.setParameterList("postIds", postIds);
                return query.list();
            });
            if (rows != null) for (Object[] row : rows) counts.put((Integer) row[0], ((Number) row[1]).intValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counts;
    }
}
