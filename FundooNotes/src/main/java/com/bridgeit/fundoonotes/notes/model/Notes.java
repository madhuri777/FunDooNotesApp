package com.bridgeit.fundoonotes.notes.model;

import java.util.ArrayList;
//import java.sql.Date;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "notes")
public class Notes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private User userid;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Labels> label = new HashSet<Labels>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<User> shareTo = new HashSet<User>();

	@OneToMany(fetch = FetchType.EAGER,mappedBy="note")
//	@JoinTable
//	  (
//	      joinColumns={ @JoinColumn(name="Notes_id", referencedColumnName="id") },
//	      inverseJoinColumns={ @JoinColumn(name="url_id", referencedColumnName="linkId", unique=true) }
//	  )
	private List<WebScrapping> url = new ArrayList<WebScrapping>();; 
	
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
	private String colour = "white";

	@Column
	private boolean archive = false;

	@Column
	private boolean trash = false;

	@Column
	private boolean pin = false;

	@Column
	private String image;

	
	public Notes() {

	}

	public Notes(NotesDTO dto) {
		this.title = dto.getTitle();
		this.discription = dto.getDiscription();
		this.archive = dto.isArchive();
		this.trash = dto.isTrash();
		this.pin = dto.isPin();
		this.reminder = dto.getReminder();
		this.createdDate = new Date();
		this.modifiedDate = createdDate;
		this.colour = dto.getColour();
		this.label = dto.getLabel();
		this.image = dto.getImage();
	}


	public List<WebScrapping> getUrl() {
		return url;
	}

	public void setUrl(List<WebScrapping> url) {
		this.url = url;
	}

	public Set<User> getShareTo() {
		return shareTo;
	}

	public void setShareTo(Set<User> shareTo) {
		this.shareTo = shareTo;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
