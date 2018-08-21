package com.bridgeit.fundoonotes.labels.model;

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

import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="label")
public class Labels {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long labelId;
	
	@Column
	private String labelName;
	
	@JsonIgnore
	@ManyToMany(mappedBy="label",fetch = FetchType.EAGER)
	private Set<Notes> note=new HashSet<Notes>();
	
	@JsonProperty(access=Access.WRITE_ONLY)
	@ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private User userid;
	
	public long getLabelId() {
		return labelId;
	}
	public void setLabelId(long labelId) {
		this.labelId = labelId;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	public User getUser() {
		return userid;
	}
	public void setUser(User userid) {
		this.userid = userid;
	}
	public Set<Notes> getNote() {
		return note;
	}
	public void setNote(Set<Notes> note) {
		this.note = note;
	}

	
}
