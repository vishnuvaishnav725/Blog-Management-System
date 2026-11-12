package com.blog.daoimpl;

import java.util.List;



import org.hibernate.HibernateException;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blog.dao.UserInfoDao;
import com.blog.pojo.UserInfo;

@Component
public class UserInfoDaoImpl implements UserInfoDao {

	private HibernateTemplate hTemplate;

	@Autowired
	public void sethTemplate(HibernateTemplate hTemplate) {
		this.hTemplate = hTemplate;
	}

	
	@Override
	public UserInfo checkUserCredential(String email, String password) {
	    try {
	        UserInfo user = hTemplate.execute(new HibernateCallback<UserInfo>() {
	            @Override
	            public UserInfo doInHibernate(Session session) throws HibernateException {
	                
Query q = session.createQuery("from UserInfo where email =:em and password =:pass");
	                
	                q.setParameter("em", email);
	                q.setParameter("pass", password);

	                List<UserInfo> lst = q.getResultList();

	                if (lst.size() > 0)
	                    return lst.get(0);
	                else
	                    return null;
	            }
	        });

	        if (user != null)
	            return user;
	        else
	            return null;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	
	@Override
	@Transactional
	public boolean addUser(UserInfo u) {
		try {
			hTemplate.save(u);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

	@Override
	@Transactional
	public boolean updateUser(UserInfo u) {
	
     try {
		   hTemplate.update(u);
		   return true;
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
	}

	

	@Override
	@Transactional
	public boolean deleteUser(int id) {
	
		try {
			UserInfo user = hTemplate.get(UserInfo.class, id);
			if(user!=null) {
				hTemplate.delete(user);
				return true;
			}
			return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	@Transactional(readOnly=true)
	public UserInfo getUserById(int id) {
		
		try {
			UserInfo u = hTemplate.get(UserInfo.class, id);
			System.out.println("u======"+u);
			return u;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserInfo> getAllUser() {
		
		try {
			List<UserInfo> lst = hTemplate.loadAll(UserInfo.class);
			return lst;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@Override
	@Transactional
	public boolean blockUser(int id) {
		
		try {
			  UserInfo user = hTemplate.get(UserInfo.class, id);
			  if(user !=null) {
				 user.setBlocked(true);
				 hTemplate.update(user);
				  return true;
			  }
			  
			  return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	@Transactional
	public boolean unblockUser(int id) {
		
		try {
			
			UserInfo user = hTemplate.get(UserInfo.class, id);
			if(user!=null) {
				user.setBlocked(false);
				hTemplate.update(user);
				return true;
			}
			    return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	
	
	
	
}
