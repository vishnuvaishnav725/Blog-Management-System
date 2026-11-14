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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blog.dao.UserInfoDao;
import com.blog.pojo.UserInfo;

@Component
public class UserInfoDaoImpl implements UserInfoDao {

	private HibernateTemplate hTemplate;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	public void sethTemplate(HibernateTemplate hTemplate) {
		this.hTemplate = hTemplate;
	}

	
	@Override
	@Transactional
	public UserInfo checkUserCredential(String email, String password) {
	    if (email == null || password == null) {
	        return null;
	    }
	    try {
	        UserInfo user = hTemplate.execute(session -> {
	            Query<UserInfo> query = session.createQuery(
	                    "from UserInfo where email = :email", UserInfo.class);
	            query.setParameter("email", email);
	            return query.uniqueResult();
	        });

	        if (user == null || user.getPassword() == null) {
	            return null;
	        }

	        String storedPassword = user.getPassword();
	        if (isBCryptHash(storedPassword)) {
	            return passwordEncoder.matches(password, storedPassword) ? user : null;
	        }

	        // Upgrade existing plaintext credentials at the next successful login.
	        if (storedPassword.equals(password)) {
	            user.setPassword(passwordEncoder.encode(password));
	            hTemplate.update(user);
	            return user;
	        }
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public String encodePassword(String rawPassword) {
	    return passwordEncoder.encode(rawPassword);
	}

	private boolean isBCryptHash(String password) {
	    return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
	}

	
	
	@Override
	@Transactional
	public boolean addUser(UserInfo u) {
		try {
			u.setPassword(passwordEncoder.encode(u.getPassword()));
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
		   if (u.getPassword() != null && !isBCryptHash(u.getPassword())) {
		       u.setPassword(passwordEncoder.encode(u.getPassword()));
		   }
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
