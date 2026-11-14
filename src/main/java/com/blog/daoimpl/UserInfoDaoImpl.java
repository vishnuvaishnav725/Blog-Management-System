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

	        // Plain-text accounts are retained as entered for this local project.
	        return storedPassword.equals(password) ? user : null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Transactional
	public boolean changePassword(int userId, String currentPassword, String newPassword) {
	    if (currentPassword == null || newPassword == null) {
	        return false;
	    }
	    try {
	        UserInfo user = hTemplate.get(UserInfo.class, userId);
	        if (user == null || user.getPassword() == null) {
	            return false;
	        }
	        String storedPassword = user.getPassword();
	        boolean matches = isBCryptHash(storedPassword)
	                ? passwordEncoder.matches(currentPassword, storedPassword)
	                : storedPassword.equals(currentPassword);
	        if (!matches) {
	            return false;
	        }
	        user.setPassword(newPassword);
	        hTemplate.update(user);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Transactional(readOnly = true)
	public boolean isEmailUsedByAnotherUser(String email, int userId) {
	    try {
	        Long count = hTemplate.execute(session -> {
	            Query<Long> query = session.createQuery(
	                    "select count(u.id) from UserInfo u where lower(u.email) = lower(:email) and u.id <> :id",
	                    Long.class);
	            query.setParameter("email", email);
	            query.setParameter("id", userId);
	            return query.uniqueResult();
	        });
	        return count != null && count > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return true;
	    }
	}

	private boolean isBCryptHash(String password) {
	    return password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$");
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
