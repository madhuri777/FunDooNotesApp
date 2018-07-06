package com.bridgeit.fundoonotes.notes.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	Notes notes;
	
	
	@Autowired
	NotesDTO noteDTO;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	NotesDAO notesDAO;
	
	@Override
	@Transactional
	public NotesDTO createNotes(String tocken,NotesDTO dto) {
		
		long id=JWT.parseJWT(tocken);
		
		User user=userDao.getUserById(id);
		
		Notes note=new Notes(dto);
		note.setUserid(user);
		
		long noteid=notesDAO.save(note);
		
		note=notesDAO.getNoteById(noteid);
		
		noteDTO.setTitle(note.getTitle());
		noteDTO.setDiscription(note.getDiscription());
		noteDTO.setArchive(note.getArchive());
		noteDTO.setPin(note.isPin());
		noteDTO.setTrash(note.getTrash());
	    noteDTO.setColour(note.getColour());
		return noteDTO;
	}

	@Override
	@Transactional
	public List<NotesDTO> getAllNotes(String token) {
		
		System.out.println("sssss notes service "+token);
		
		List<NotesDTO> liNotesDTOs=new ArrayList<NotesDTO>();
		long id=JWT.parseJWT(token);
		
		User user=userDao.getUserById(id);
		
		System.out.println("qqqq "+user);
		
		List<Notes> listNote=notesDAO.getAllNotes(user);
		
		for(Notes note : listNote){
			
			NotesDTO notesDTO1=new NotesDTO();
			notesDTO1.setTitle(note.getTitle());
			notesDTO1.setDiscription(note.getDiscription());
			notesDTO1.setArchive(note.getArchive());
			notesDTO1.setTrash(note.getTrash());
			notesDTO1.setPin(note.isPin());
			
			liNotesDTOs.add(notesDTO1);
			
		}
		System.out.println("after dao in service ");
		
		return  liNotesDTOs;
	}

	@Override
	@Transactional
	public boolean update(long id, String token,NotesDTO dto) {
		Notes note=notesDAO.getNoteById(id);
		System.out.println("update service "+note);
		long userid=note.getUserid().getUserId();
		System.out.println("user id "+userid);
		long tokenid=JWT.parseJWT(token);
		System.out.println("token id "+tokenid);
		if(userid==tokenid) {
			Notes notes=new Notes(dto);
//			note.setTitle(dto.getTitle());
//            note.setDiscription(dto.getDiscription());
//            note.setTrash(dto.isTrash());
//            note.setColour(dto.getColour());
//            note.setArchive(dto.isArchive());
//            note.setPin(dto.isPin());
            notes.setModifiedDate(new Date());
			notesDAO.update(notes);
			System.out.println("update success in service ");
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean delete(long noteid, String token) {
		
		boolean status=false;
		
		long userid=JWT.parseJWT(token);
		User user=userDao.getUserById(userid);
		Notes note=notesDAO.getNoteById(noteid);
		System.out.println("usercha id userdatabase madhun "+user.getUserId());
		
		System.out.println("usercha id from dileleya token madhun "+userid);
		
		if(userid==note.getUserid().getUserId()) {
			
			status=notesDAO.deletNoteById(noteid);
			
			return status;
		}
		
		
		return status;
	}

}
