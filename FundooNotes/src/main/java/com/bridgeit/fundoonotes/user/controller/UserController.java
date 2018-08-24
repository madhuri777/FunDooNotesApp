package com.bridgeit.fundoonotes.user.controller;

import java.util.List;

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
import com.bridgeit.fundoonotes.user.model.Response;
import com.bridgeit.fundoonotes.user.model.UserDTO;
import com.bridgeit.fundoonotes.user.service.IUserService;


@RestController
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ResponseEntity<?> save(@Validated @RequestBody RegistrationDTO registeruser, HttpServletRequest request,Response res) {

		
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath()
				+ "/activeuser/";
		
		boolean flag=userService.register(registeruser, url);

		res.setMessage("Registration Done");
		//if (flag) {
			return new ResponseEntity<Response>(res,HttpStatus.OK);
//		}
//		return new ResponseEntity<String>("Registrion Not Done",HttpStatus.CONFLICT);

	}

	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody LoginDTO loginuser, HttpServletResponse response,Response response2) {
      
		 response2=userService.login(loginuser);
		if(response2.getUserdto()!=null) {
			response2.setMessage("User logged successfully");
			return new ResponseEntity<Response>(response2, HttpStatus.OK);
		
		}else {
			response2.setMessage("User Not Found Or User Not Activated");
			return new ResponseEntity<Response>(response2,HttpStatus.CONFLICT);
		}
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
	public ResponseEntity<?> forgetPassWord(@RequestBody ForgetPassWordDTO forgetpasswd,
			HttpServletRequest request,Response res) {

		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "" + request.getContextPath()
				+ "/changepassword/";

		LOGGER.info("url " + url);

		String emailId = forgetpasswd.getEmail();

		if (userService.forgetPassWord(emailId, url)) {
            res.setMessage("Link Sent For Reset the passWord");
			return new ResponseEntity<Response>(res, HttpStatus.OK);
		}
		res.setMessage("User is Not verifed");
		return new ResponseEntity<Response>(res, HttpStatus.CONFLICT);
	}

	
	@RequestMapping(value="/changepassword/{token:.+}",method=RequestMethod.GET)
	public ResponseEntity<?> changePassWord(@PathVariable("token") String token,HttpServletResponse res) throws Exception{
		System.out.println("changePassWord");
		boolean flag=userService.changePassWord(token,res);
		System.out.printf("flag ",flag);
		if(flag) {
			
		return new ResponseEntity<String>("ok",HttpStatus.OK); 
	}
	    return new ResponseEntity<String>("Bad Request",HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/resetpassword/{tocken:.+}", method = RequestMethod.PUT)
	public ResponseEntity<?> resetPassWord(@PathVariable("tocken") String tocken, @RequestBody ForgetPassWordDTO password,Response res) {

		System.out.println("reset service backend "+tocken);
		boolean flag = userService.resetPassWord(tocken, password.getNewPassWord());

		if (flag) {

			res.setMessage("PssWord Set Successfuly");
			return new ResponseEntity<Response>(res, HttpStatus.ACCEPTED);
		}else {
			res.setMessage("Can't ResetPassWord");
		return new ResponseEntity<Response>(res, HttpStatus.CONFLICT);
	}
	}
	
	@RequestMapping(value="/getAllUsers",method=RequestMethod.GET)
	public ResponseEntity<?> getAllUsers(HttpServletRequest request){
		
		String token=request.getHeader("userid");
		System.out.println("token of user "+token);
		List<UserDTO> listOfuser=userService.getAllUsers(token);
		return new ResponseEntity<>(listOfuser,HttpStatus.OK);
	}
	
	@RequestMapping(value="/upadteuserprofile",method=RequestMethod.POST)
	public ResponseEntity<?> upadteUserProfile(@RequestBody UserDTO dto,Response response){
		
		UserDTO dto2=userService.updateUserProfile(dto);
		response.setMessage("successfully");
		response.setUserdto(dto2);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}