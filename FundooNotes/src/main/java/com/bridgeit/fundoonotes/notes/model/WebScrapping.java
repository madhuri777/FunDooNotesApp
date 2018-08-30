package com.bridgeit.fundoonotes.notes.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="WebScrap")
public class WebScrapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long linkId;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column
	private String image;

	@ManyToOne
	@JoinColumn(name="noteid", nullable=false)
	@JsonIgnore
	private Notes note;
	
	
	public Notes getNote() {
		return note;
	}
	public void setNote(Notes note) {
		this.note = note;
	}
	public long getLinkId() {
		return linkId;
	}
	public void setLinkId(long linkId) {
		this.linkId = linkId;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	@Override
	public String toString() {
		return "WebScrapping [id=" + linkId + ", title=" + title + ", description=" + description + ", image=" + image
				+ "]";
	}
	
	
}
