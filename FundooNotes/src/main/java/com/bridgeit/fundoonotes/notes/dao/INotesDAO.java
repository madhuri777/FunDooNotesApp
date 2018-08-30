package com.bridgeit.fundoonotes.notes.dao;

import java.util.List;

import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.WebScrapping;
import com.bridgeit.fundoonotes.user.model.User;

public interface INotesDAO {

	long save(Notes notes);
	List<Notes> getAllNotes(User user);
	Notes getNoteById(long noteid);
	boolean update(Notes notes);
	boolean deletNoteById(long id);
	List<Notes> getAllCollaboratorNotes(User user);
	long saveWeb(WebScrapping web);
}
