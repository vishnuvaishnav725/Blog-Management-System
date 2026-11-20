package com.blog.daoimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blog.dao.FollowDao;
import com.blog.pojo.Follow;

@Component
public class FollowDaoImpl implements FollowDao {
	@Autowired
	private HibernateTemplate hTemplate;

	@Override
	@Transactional
	public boolean follow(int followerId, int followedId) {
		if (followerId <= 0 || followedId <= 0 || followerId == followedId) return false;
		try {
			if (isFollowing(followerId, followedId)) return true;
			hTemplate.save(new Follow(followerId, followedId));
			hTemplate.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean unfollow(int followerId, int followedId) {
		try {
			Integer deleted = hTemplate.execute(session -> {
				Query<?> query = session.createQuery(
						"delete from Follow f where f.followerId = :follower and f.followedId = :followed");
				query.setParameter("follower", followerId);
				query.setParameter("followed", followedId);
				return query.executeUpdate();
			});
			return deleted != null && deleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isFollowing(int followerId, int followedId) {
		try {
			Long count = hTemplate.execute(session -> {
				Query<Long> query = session.createQuery(
						"select count(f.id) from Follow f where f.followerId = :follower and f.followedId = :followed",
						Long.class);
				query.setParameter("follower", followerId);
				query.setParameter("followed", followedId);
				return query.uniqueResult();
			});
			return count != null && count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Integer> getFollowedIds(int followerId) {
		try {
			List<Integer> ids = hTemplate.execute(session -> {
				Query<Integer> query = session.createQuery(
						"select f.followedId from Follow f where f.followerId = :follower", Integer.class);
				query.setParameter("follower", followerId);
				return query.list();
			});
			return ids == null ? new HashSet<>() : new HashSet<>(ids);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashSet<>();
		}
	}
}
