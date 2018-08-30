package com.bridgeit.fundoonotes.notes.service;

import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;

import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.model.WebScrapping;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.model.UserDTO;

public interface INotesService {

	NotesDTO createNotes(String token,NotesDTO dto);
	List<NotesDTO> getAllNotes(String token);
	boolean update(long id,String token,NotesDTO dto);
	boolean delete(long id,String token);
	Resource loadFile(String fileName);
	boolean collaborator(String email,NotesDTO dto);
	Set<UserDTO> getAllUserList(Set<User> list);
	void removeCollaborator(long userId,NotesDTO dto);
	List<WebScrapping> webScrap(String url,long noteid);
}
