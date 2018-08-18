package com.bridgeit.fundoonotes.user.service;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.bridgeit.fundoonotes.user.model.LoginDTO;
import com.bridgeit.fundoonotes.user.model.RegistrationDTO;
import com.bridgeit.fundoonotes.user.model.UserDTO;


public interface IUserService {

	boolean register(RegistrationDTO user,String url);
	String login(LoginDTO user);
	boolean verify(String tocken);
	boolean forgetPassWord(String email,String url);
	boolean resetPassWord(String tocken,String newPassWd);
	boolean changePassWord(String token,HttpServletResponse res) throws Exception;
	List<UserDTO> getAllUsers();
}
