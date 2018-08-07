package com.bridgeit.fundoonotes.notes.model;

//import java.sql.Date;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.model.User;

@Entity
@Table(name="notes")
public class Notes {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	private User userid;
	
	@ManyToMany(cascade=CascadeType.ALL ,fetch = FetchType.EAGER)
	private Set<Labels> label=new HashSet<Labels>();
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;
	
	@Column
	private Date reminder;
	
	@Column
	private String title;
	
	@Column
	private String discription;
	
	@Column
	private String colour="white";
	
	@Column
	private boolean archive=false;
	
	@Column
	private boolean trash=false;
	
	@Column
	private boolean pin=false;
	
	public Notes() {
		
	}
	public Notes(NotesDTO dto) {
		this.title=dto.getTitle();
		this.discription=dto.getDiscription();
	    this.archive=dto.isArchive();
	    this.trash=dto.isTrash();
	    this.pin=dto.isPin();
	    this.reminder=dto.getReminder();
	    this.createdDate=new Date();
	    this.modifiedDate=createdDate;
	    this.colour=dto.getColour();
	    this.label=dto.getLabel();
	}
	
	public boolean isPin() {
		return pin;
	}
	public void setPin(boolean pin) {
		this.pin = pin;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUserid() {
		return userid;
	}
	public void setUserid(User userid) {
		this.userid = userid;
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
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public boolean getArchive() {
		return archive;
	}
	public void setArchive(boolean archive) {
		this.archive = archive;
	}
	public boolean getTrash() {
		return trash;
	}
	public void setTrash(boolean trash) {
		this.trash = trash;
	}
	public Date getReminder() {
		return reminder;
	}
	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}
	public Set<Labels> getLabel() {
		return label;
	}
	public void setLabel(Set<Labels> label) {
		this.label = label;
	}
	@Override
	public String toString() {
		return "Notes [id=" + id + ", userid=" + userid + ", label=" + label + ", createdDate=" + createdDate
				+ ", modifiedDate=" + modifiedDate + ", reminder=" + reminder + ", title=" + title + ", discription="
				+ discription + ", colour=" + colour + ", archive=" + archive + ", trash=" + trash + ", pin=" + pin
				+ "]";
	}
	
	
}
