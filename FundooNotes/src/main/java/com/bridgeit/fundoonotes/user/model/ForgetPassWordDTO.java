package com.bridgeit.fundoonotes.user.model;

public class ForgetPassWordDTO {

	private String email;
	
	private String newPassWord;
	
	private String conformPassWord;
	
	

	public String getNewPassWord() {
		return newPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}

	public String getConformPassWord() {
		return conformPassWord;
	}

	public void setConformPassWord(String conformPassWord) {
		this.conformPassWord = conformPassWord;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
