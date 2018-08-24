package com.bridgeit.fundoonotes.user.model;

public class UserDTO {

	private long userId;
	
	private String username;
	
	private String emailId;
	
	private String profile;
	

	//default constructore
	public UserDTO() {
		
	}
	
	//Parameterize constructor
	public UserDTO(User user) {
		this.userId=user.getUserId();
		this.username=user.getName();
		this.emailId=user.getEmail();
		this.profile=user.getProfile();
	}
	
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
//
//	@Override
//	public String toString() {
//		return "UserDTO [userId=" + userId + ", username=" + username + ", emailId=" + emailId + "]";
//	}
//	
//	
	
}
