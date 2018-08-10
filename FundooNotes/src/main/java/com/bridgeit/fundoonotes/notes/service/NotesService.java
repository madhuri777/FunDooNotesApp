package com.bridgeit.fundoonotes.notes.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.notes.dao.NotesDAO;
import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.user.dao.UserDao;
import com.bridgeit.fundoonotes.user.exception.DataBaseException;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class NotesService implements INotesService {

	private final Path rootLocation = Paths.get("/home/bridgeit/eclipse-workspace-FundoApp/FundooNotes/src/main/java/com/bridgeit/fundoonotes/Profile/");
	
	
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
		System.out.println("get all note "+listNote);
		
		for(Notes note : listNote){
			
			System.out.println("notes with list "+note);
			
			NotesDTO notesDTO1=new NotesDTO();
			notesDTO1.setNoteid(note.getId());
			notesDTO1.setTitle(note.getTitle());
			notesDTO1.setDiscription(note.getDiscription());
			notesDTO1.setArchive(note.getArchive());
			notesDTO1.setTrash(note.getTrash());
			notesDTO1.setPin(note.isPin());
			notesDTO1.setColour(note.getColour());
			notesDTO1.setReminder(note.getReminder());
			notesDTO1.setLabel(note.getLabel());
			liNotesDTOs.add(notesDTO1);
			
		}
		System.out.println("after dao in service ");
		
		return  liNotesDTOs;
	}

	@Override
	@Transactional
	public boolean update(long id, String token,NotesDTO dto)throws DataBaseException {
		
		//long noteid=dto.getNoteid();
		
		System.out.println("note "+id);
		
		Notes note=notesDAO.getNoteById(id);
		
		long userid=note.getUserid().getUserId();
		System.out.println("userid "+userid);
		
		long tokenid=JWT.parseJWT(token);
		
		if(userid==tokenid) {
			
			
			Notes notes=new Notes(dto);
            notes.setModifiedDate(new Date());
			
            note.setTitle(notes.getTitle());
            note.setDiscription(notes.getDiscription());
            note.setArchive(notes.getArchive());
            note.setColour(notes.getColour());
            note.setPin(notes.isPin());
            note.setTrash(notes.getTrash());
            note.setModifiedDate(notes.getModifiedDate());
            note.setReminder(notes.getReminder());
            note.setImage(notes.getImage());
            
            notesDAO.update(note);
			
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean delete(long noteid, String token)throws DataBaseException {
		
		boolean status=false;
		
		long userid=JWT.parseJWT(token);
		
		Notes note=notesDAO.getNoteById(noteid);
		
		if(userid==note.getUserid().getUserId()) {
			
			status=notesDAO.deletNoteById(noteid);
			
			return status;
		}
		return status;
	}
	
	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

}
