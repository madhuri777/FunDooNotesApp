package com.bridgeit.fundoonotes.notes.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.bridgeit.fundoonotes.user.model.UserDTO;
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
		
		List<NotesDTO> liNotesDTOs=new ArrayList<NotesDTO>();
		
		long id=JWT.parseJWT(token);
		
		User user=userDao.getUserById(id);
		
		List<Notes> listNote=notesDAO.getAllNotes(user);

		Set<UserDTO> listOfShareTO=new HashSet<>();
		
		
		List<Notes> listOfCollaboratorNotes=notesDAO.getAllCollaboratorNotes(user);
		
		for(Notes note:listOfCollaboratorNotes) {
			
			listNote.add(note);
		}
       		
		System.out.println("All together list "+listNote);
		
		
		for(Notes note : listNote){
			
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
			notesDTO1.setImage(note.getImage());
			notesDTO1.setUserName(note.getUserid().getName());
			notesDTO1.setUserEmail(note.getUserid().getEmail());
			
			listOfShareTO=getAllUserList(note.getShareTo());
			
			notesDTO1.setshareTo(listOfShareTO);
			
			liNotesDTOs.add(notesDTO1);
			
		}
		return  liNotesDTOs;
	}
	
	public Set<UserDTO> getAllUserList(Set<User> list){
		
		Set<UserDTO> listUser=new HashSet<>();
		
		for(User usr:list) {
			
			UserDTO dto=new UserDTO();
			
			dto.setEmailId(usr.getEmail());
			dto.setUserId(usr.getUserId());
			dto.setUsername(usr.getName());
			
			listUser.add(dto);
			
		}
		
		return listUser;
	}

	@Override
	@Transactional
	public boolean update(long id, String token,NotesDTO dto)throws DataBaseException {
		
		Notes note=notesDAO.getNoteById(id);
		
		long userid=note.getUserid().getUserId();
		
		long tokenid=JWT.parseJWT(token);
		
		if(userid==tokenid) {
			
			//passing DTO data in constructor of Notes
			Notes notes=new Notes(dto);
            notes.setModifiedDate(new Date());
			
            //Again setting NewNote data into Old database's Note
            note.setTitle(notes.getTitle());
            note.setDiscription(notes.getDiscription());
            note.setArchive(notes.getArchive());
            note.setColour(notes.getColour());
            note.setPin(notes.isPin());
            note.setTrash(notes.getTrash());
            note.setModifiedDate(notes.getModifiedDate());
            note.setReminder(notes.getReminder());
            note.setImage(dto.getImage());
            
            notesDAO.update(note);
            
            System.out.println("upadted note "+note);
			
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

	@Transactional
	@Override
	public boolean collaborator(String email,NotesDTO dto) {
		
		boolean status=false;
		
		Set<User> listOfUser=new HashSet<User>();
		
		Notes notes=notesDAO.getNoteById(dto.getNoteid());
			
		User user=userDao.isExist(email);
		
		if(user!=null) {
			
			listOfUser=notes.getShareTo();
			
			listOfUser.add(user);
			
			notes.setShareTo(listOfUser);
			status=notesDAO.update(notes);
			
			System.out.println("value of status "+status );
			
			return status;
		}
		
		return status;
		
		
	}
	
	@Override
	@Transactional
	public void removeCollaborator(long userId,NotesDTO dto) {
		
	// Set<User> listOfCOllaborator=new HashSet<User>();
		
     long noteId=dto.getNoteid();
     
     Notes note=notesDAO.getNoteById(noteId);
     
     Set<User> listOfCOllaborator=note.getShareTo();
     
     for(User user:listOfCOllaborator) {
    	 
    	 if(user.getUserId()==userId) {
    		 
    		 listOfCOllaborator.remove(user);
    		
    		 System.out.println("removed the user "); 
    		 
    	 }
    	 
    	 
     }
//     note.setShareTo(listOfCOllaborator);
//     notesDAO.update(note);
     
     System.out.println("user list "+listOfCOllaborator.toString()); 
     
		
	}

}
