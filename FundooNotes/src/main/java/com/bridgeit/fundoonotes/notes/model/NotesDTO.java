package com.bridgeit.fundoonotes.notes.model;

public class NotesDTO {

	private long noteid;
	public long getNoteid() {
		return noteid;
	}

	public void setNoteid(long noteid) {
		this.noteid = noteid;
	}

	private String title;
	
	private String discription;
	
	private boolean archive=false;
	
	private boolean trash=false;
	
	private boolean pin=false;
	
	private String colour;

    public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public NotesDTO() {
    	
    }
//    public NotesDTO(Notes notes) {
//    	this.title=notes.getTitle();
//    	this.discription=notes.getDiscription();
//    	this.archive=notes.getArchive();
//    	this.trash=notes.getTrash();
//    	this.pin=notes.isPin();
//    }
	
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
