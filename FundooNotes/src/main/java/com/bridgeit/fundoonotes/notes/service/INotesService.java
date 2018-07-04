package com.bridgeit.fundoonotes.notes.service;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;

public interface INotesService {

	boolean createNotes(String token,NotesDTO dto);
	
}
