//package com.bridgeit.fundoonotes.notes.model;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import com.bridgeit.fundoonotes.user.model.User;
//
//@Entity
//@Table(name="collaborator")
//public class Collaborator {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	private long id;
//
//	@OneToOne(targetEntity=User.class)
//	private User shareBy;
//
//	@OneToMany(fetch=FetchType.EAGER)
//	//@JoinTable(name="collaborator",joinColumns= {@JoinColumn(name="userId")},inverseJoinColumns= {@JoinColumn(name="id")})
//	private Set<User> shareTo=new  HashSet<User>();
//	
//	@ManyToMany(mappedBy="collaborator",fetch = FetchType.EAGER)
//	private Set<Notes> noteid=new HashSet<Notes>();
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//
////	public User getShareBy() {
////		return shareBy;
////	}
////
////	public void setShareBy(User shareBy) {
////		this.shareBy = shareBy;
////	}
//
//	public Set<User> getShareTo() {
//		return shareTo;
//	}
//
//	public void setShareTo(Set<User> shareTo) {
//		this.shareTo = shareTo;
//	}
//
//	public Set<Notes> getNoteid() {
//		return noteid;
//	}
//
//	public void setNoteid(Set<Notes> noteid) {
//		this.noteid = noteid;
//	}
//	
//	
//}
