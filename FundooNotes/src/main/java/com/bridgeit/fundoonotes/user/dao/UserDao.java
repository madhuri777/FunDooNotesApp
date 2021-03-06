package com.bridgeit.fundoonotes.user.dao;

import java.util.List;
import java.util.Set;

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
		System.out.println("email id in is Exist method "+email); 
		

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("email", email));
		User user2 = (User) criteria.uniqueResult();

		System.out.println("user for collaborator in dao "+user2); 
	
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

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAllUsers() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		List<User> listOfUsers= criteria.list();
		
		System.out.println("List of usrs in dao "+listOfUsers.toString());
		
		return listOfUsers;
	}

}
