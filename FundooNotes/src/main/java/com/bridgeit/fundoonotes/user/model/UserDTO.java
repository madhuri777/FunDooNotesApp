package com.bridgeit.fundoonotes.user.model;

public class UserDTO {

	private long userId;
	
	private String username;
	
	private String emailId;

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
