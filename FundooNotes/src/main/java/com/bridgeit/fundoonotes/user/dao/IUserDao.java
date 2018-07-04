package com.bridgeit.fundoonotes.user.dao;

import com.bridgeit.fundoonotes.user.model.User;

public interface IUserDao {

	long register(User user);
	User isExist(String user);
	boolean update(User user);
	User getUserById(long id);
}
