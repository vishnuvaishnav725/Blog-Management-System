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
	        try {
	            List<Post> posts = hTemplate.loadAll(Post.class);
	           return posts;
	        } catch (Exception e) {
	            e.printStackTrace();                    
	            return null;
	        }
	    }

	
	
	
	
	
	

}
