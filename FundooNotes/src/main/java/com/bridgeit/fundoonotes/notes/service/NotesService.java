package com.bridgeit.fundoonotes.notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.notes.dao.NotesDAO;
import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.user.dao.UserDao;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class NotesService implements INotesService {

	Notes note;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	NotesDAO notesDAO;
	
	@Override
	@Transactional
	public boolean createNotes(String tocken,NotesDTO dto) {
		
		long id=JWT.parseJWT(tocken);
		User user=userDao.getUserById(id);
		dto.setUserid(user);
		Notes note=new Notes(dto);
		notesDAO.save(note);
		return true;
	}

}
