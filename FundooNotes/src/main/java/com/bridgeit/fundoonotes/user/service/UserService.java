package com.bridgeit.fundoonotes.user.service;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.user.configuration.FundooNotesConfiguration;
import com.bridgeit.fundoonotes.user.dao.IUserDao;
import com.bridgeit.fundoonotes.user.dao.TokenUtil;
import com.bridgeit.fundoonotes.user.exception.DataBaseException;
import com.bridgeit.fundoonotes.user.exception.LoginException;
import com.bridgeit.fundoonotes.user.model.EmailTocken;
import com.bridgeit.fundoonotes.user.model.LoginDTO;
import com.bridgeit.fundoonotes.user.model.RegistrationDTO;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class UserService implements IUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private IUserDao userDao;

	EmailTocken emailTocken = new EmailTocken();

	User user;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	TokenUtil tokenUtil;

	@Override
	@Transactional
	public boolean register(RegistrationDTO registeruser, String url)throws DataBaseException {

		String pw_hash = BCrypt.hashpw(registeruser.getPassword(), BCrypt.gensalt());

		user = new User();
		user.setName(registeruser.getName());
		user.setEmail(registeruser.getEmail());
		user.setPassword(pw_hash);
		user.setPhoneNumber(registeruser.getPhoneNumber());

		User user2 = userDao.isExist(user.getEmail());

		if (user2 == null) {

			long id = userDao.register(user);

			String tocken = JWT.createJWT(id + "", 3600000);
			System.out.println("qqq " + tocken);

			String tockenUrl = url + tocken;

			emailTocken.setEmail(user.getEmail());
			emailTocken.setTockenurl(tockenUrl);
			emailTocken.setSubject("Verify User For Login");

			rabbitTemplate.convertAndSend(FundooNotesConfiguration.topicExchangeName, "lazy.orange.rabbit",
					emailTocken);

			tokenUtil.setToken(id + "", tocken);

			return true;
		}else
			
		throw new DataBaseException("Email Already Exist");
		
	}

	@Override
	@Transactional
	public String login(LoginDTO loginuser) throws LoginException {

		String token;

		User user2 = userDao.isExist(loginuser.getEmail());

		if (user2 != null && user2.isVerified() == true
				&& BCrypt.checkpw(loginuser.getPassword(), user2.getPassword())) {

			long id = user2.getUserId();
			token = JWT.createJWT(String.valueOf(id), 86400000);

			return token;
		} 
//		else
//			throw new LoginException("User Not Found"); // return null;
		return null;
	}

	@Override
	@Transactional
	public boolean verify(String token) {

		boolean status = false;
		long id = JWT.parseJWT(token);

		String redisToken = tokenUtil.getToken(id + "");
		LOGGER.info("redis in verify " + redisToken);

		if (token.equals(redisToken)) {

			User user = userDao.getUserById(id);

			user.setVerified(true);

			status = userDao.update(user);

			tokenUtil.deleteUser(String.valueOf(id));
		}
		return status;
	}

	@Override
	@Transactional
	public boolean forgetPassWord(String email, String url)throws LoginException {

		User user = userDao.isExist(email);

		if (user != null && user.isVerified() == true) {

			long id = user.getUserId();

			String token = JWT.createJWT(id + "", 3600000);

			String tokenurl = url + token;

			emailTocken.setEmail(user.getEmail());
			emailTocken.setTockenurl(tokenurl);
			emailTocken.setSubject("Link For Reset PassWord");

			rabbitTemplate.convertAndSend(FundooNotesConfiguration.topicExchangeName, "lazy.orange.rabbit",
					emailTocken);

			tokenUtil.setToken(String.valueOf(id), token);

			return true;
		}else
			
			throw new LoginException("Not Verified User");

	}

	@Override
	@Transactional
	public boolean resetPassWord(String tocken, String newPass) {

		boolean status = false;

		long id = JWT.parseJWT(tocken);

		String redistoken = tokenUtil.getToken(String.valueOf(id));

		if (tocken.equals(redistoken)) {

			User user = userDao.getUserById(id);

			String pw_hash = BCrypt.hashpw(newPass, BCrypt.gensalt());

			user.setPassword(pw_hash);

			status = userDao.update(user);

		}

		return status;
	}

	@Override
	public boolean changePassWord(String token,HttpServletResponse res) throws Exception {
		long id = JWT.parseJWT(token);

		String redistoken = tokenUtil.getToken(String.valueOf(id));
		
		if(token.equals(redistoken)) {
			
			System.out.println("send redisrect method");
			res.sendRedirect("http://127.0.0.1:8081/#!/resetPassword?token="+token);
			return true;
		}
		return false;
	}

}
