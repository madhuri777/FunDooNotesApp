package com.bridgeit.fundoonotes.notes.service;

import java.util.List;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;

public interface INotesService {

	NotesDTO createNotes(String token,NotesDTO dto);
	List<NotesDTO> getAllNotes(String token);
	boolean update(long id,String token,NotesDTO dto);
	boolean delete(long id,String token);
}
