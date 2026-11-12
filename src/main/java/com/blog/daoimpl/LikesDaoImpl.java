package com.blog.daoimpl;

import java.util.List;

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
}
