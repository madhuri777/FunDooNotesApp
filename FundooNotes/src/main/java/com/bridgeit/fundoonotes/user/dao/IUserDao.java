package com.bridgeit.fundoonotes.user.dao;

import java.util.List;
import java.util.Set;

import com.bridgeit.fundoonotes.user.model.User;

public interface IUserDao {

	long register(User user);
	User isExist(String user);
	boolean update(User user);
	User getUserById(long id);
	List<User> getAllUsers();
}
