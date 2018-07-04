package com.bridgeit.fundoonotes.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.bridgeit.fundoonotes.user.model.ForgetPassWordDTO;
import com.bridgeit.fundoonotes.user.model.LoginDTO;
import com.bridgeit.fundoonotes.user.model.RegistrationDTO;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.service.IUserService;

@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ResponseEntity<User> save(@Validated @RequestBody RegistrationDTO registeruser, HttpServletRequest request) {

		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath()
				+ "/activeuser/";

		if (userService.register(registeruser, url)) {
			return new ResponseEntity<User>(HttpStatus.OK);
		} else {

			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}

	}

	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody LoginDTO loginuser, HttpServletResponse response) {
      
		String tocken=userService.login(loginuser);
		if (tocken!=null) {

			return new ResponseEntity<String>("Login in Successfully  and ur tocken id==> \n "+tocken, HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("Invalid User Or Not Verified User", HttpStatus.CONFLICT);
	}

	
	
	@RequestMapping(value = "/activeuser/{token:.+}", method = RequestMethod.GET)
	public ResponseEntity<String> activeUser(@PathVariable("token") String token) {
		// if return false put statement
		boolean status = userService.verify(token);

		if (status) {
			return new ResponseEntity<String>("User Activated", HttpStatus.CREATED);
		}
		return new ResponseEntity<String>("User Not Activated Yet", HttpStatus.NOT_ACCEPTABLE);
	}

	
	
	@RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
	public ResponseEntity<String> forgetPassWord(@RequestBody ForgetPassWordDTO forgetpasswd,
			HttpServletRequest request) {

		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath()
				+ "/resetpassword/";

		LOGGER.info("url " + url);

		String emailId = forgetpasswd.getEmail();

		if (userService.forgetPassWord(emailId, url)) {

			return new ResponseEntity<String>("Link Sent For Reset the passWord", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("User is Not verifed", HttpStatus.CONFLICT);
	}

	
	@RequestMapping(value = "/resetpassword/{tocken:.+}", method = RequestMethod.PUT)
	public ResponseEntity<String> resetPassWord(@PathVariable("tocken") String tocken, @RequestBody String password) {

		boolean flag = userService.resetPassWord(tocken, password);

		if (flag) {

			return new ResponseEntity<String>("PssWord Set Successfuly", HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<String>("Can't ResetPassWord", HttpStatus.CONFLICT);
	}

}