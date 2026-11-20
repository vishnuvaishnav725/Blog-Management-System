package com.blog.daoimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blog.dao.PostDao;
import com.blog.pojo.Post;

@Component
public class PostDaoImpl  implements PostDao{

   
	@Autowired
	private HibernateTemplate hTemplate;

	
	public void sethTemplate(HibernateTemplate hTemplate) {
		this.hTemplate = hTemplate;
	}

	@Override
	@Transactional
	public boolean addPost(Post p) {
		
		try {
			hTemplate.save(p);
			System.out.println("HEllo ");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updatePost(Post p) {
		
		try {
			hTemplate.update(p);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deletePost(int pId) {
	    try {
	       
	        hTemplate.execute(session -> {
	            org.hibernate.query.Query<?> q = session.createQuery(
	                    "delete from Comment c where c.post.pId = :pid");
	            q.setParameter("pid", pId);
	            q.executeUpdate();
	            return null;
	        });

	       
	        Post post = hTemplate.get(Post.class, pId);
	        if (post != null) {
	            hTemplate.delete(post);
	        }

	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	@Transactional(readOnly=true)
	public Post getPostById(int pId) {
		
	  try {
		  Post p = hTemplate.get(Post.class, pId);
		  return p;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}
	   @Transactional  
	 @Override
	             
	    public List<Post> getAllPosts() {
	        return getFeedPosts(0, false);
	    }

	    @Override
	    @Transactional(readOnly = true)
	    public List<Post> getFeedPosts(int userId, boolean followedOnly) {
	        try {
	            return hTemplate.execute(session -> {
	                String hql = followedOnly
	                    ? "select p from Post p join fetch p.author a "
	                        + "where a.id in (select f.followedId from Follow f where f.followerId = :userId) "
	                        + "order by p.createdAt desc, p.pId desc"
	                    : "select p from Post p left join fetch p.author "
	                        + "order by p.createdAt desc, p.pId desc";
	                org.hibernate.query.Query<Post> query = session.createQuery(hql, Post.class);
	                if (followedOnly) query.setParameter("userId", userId);
	                return query.list();
	            });
	        } catch (Exception e) {
	            e.printStackTrace();                    
	            return null;
	        }
	    }

	
	
	
	
	
	

}
