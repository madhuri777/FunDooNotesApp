package com.bridgeit.fundoonotes.user.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.bridgeit.fundoonotes.user.exception.DataBaseException;
import com.bridgeit.fundoonotes.user.model.User;

@Repository
public class UserDao implements IUserDao {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

	@Override
	public long register(User user) {

		Session session = sessionFactory.openSession();
		long userId = (Long) session.save(user);
		return userId;
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public User isExist(String email) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("email", email));
		User user2 = (User) criteria.uniqueResult();

		return user2 != null ? user2 : null;
	}

	@Override
	public boolean update(User user) throws DataBaseException {

		sessionFactory.getCurrentSession().update(user);
		LOGGER.info("upadted ");
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public User getUserById(long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("userId", id));
		User user2 = (User) criteria.uniqueResult();
		return user2;
	}

}
