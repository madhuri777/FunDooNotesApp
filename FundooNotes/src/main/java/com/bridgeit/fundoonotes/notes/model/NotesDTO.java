package com.bridgeit.fundoonotes.notes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.model.UserDTO;

public class NotesDTO {

	private long noteid;

	private String title;
	
	private String discription;
	
	private boolean archive=false;
	
	private boolean trash=false;
	
	private boolean pin=false;
	
	private String colour;
	 
	private Date reminder;
	
	private String image;
	
	private String userName;
	
	private String userEmail;
	
	private Date modifiedDate;
	
	private String profile;
	
	private List<WebScrapping> links = new ArrayList<>();
	

	public List<WebScrapping> getLinks() {
		return links;
	}

	public void setLinks(List<WebScrapping> links) {
		this.links = links;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	private Set<UserDTO> shareTo;
	
	public Set<UserDTO> getshareTo() {
		return shareTo;
	}

	public void setshareTo(Set<UserDTO> shareTo) {
		this.shareTo = shareTo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	private Set<Labels> label=new HashSet<Labels>(); 
	
	public Set<Labels> getLabel() {
		return label;
	}

	public void setLabel(Set<Labels> label) {
		this.label = label;
	}

	public long getNoteid() {
		return noteid;
	}

	public void setNoteid(long noteid) {
		this.noteid = noteid;
	}
	
    public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isTrash() {
		return trash;
	}

	public void setTrash(boolean trash) {
		this.trash = trash;
	}

	public boolean isPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	
	
}
