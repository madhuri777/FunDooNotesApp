package com.bridgeit.fundoonotes.notes.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.notes.dao.NotesDAO;
import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.notes.model.WebScrapping;
import com.bridgeit.fundoonotes.user.dao.UserDao;
import com.bridgeit.fundoonotes.user.exception.DataBaseException;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.model.UserDTO;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class NotesService implements INotesService {

	private final Path rootLocation = Paths.get("/home/bridgeit/workspace/FunDooNotesApp/FundooNotes/src/main/java/com/bridgeit/fundoonotes/Profile/");
	
	
	@Autowired
	NotesDTO noteDTO;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	NotesDAO notesDAO;
	
	@Override
	@Transactional(propagation=Propagation.NESTED)
	public NotesDTO createNotes(String tocken,NotesDTO dto) {
		
		long id=JWT.parseJWT(tocken);
		
		User user=userDao.getUserById(id);
		
		String url=dto.getDiscription();
		long noteid2=dto.getNoteid();
		
		Notes note=new Notes(dto);
		note.setUserid(user);
		//note.setUrl(listOfLinks);
		
		long noteid=notesDAO.save(note);
		List<WebScrapping> listOfLinks=webScrap(url,noteid);
		
//		for(WebScrapping u:listOfLinks){
//			//System.out.println(u.getLinkId());
//			System.out.println("qazqazqazqazqazqazqazqazazaz "+u.toString()); 
//			
//		}
		
		
		note=notesDAO.getNoteById(noteid);
		
		noteDTO.setTitle(note.getTitle());
		noteDTO.setDiscription(note.getDiscription());
		noteDTO.setArchive(note.getArchive());
		noteDTO.setPin(note.isPin());
		noteDTO.setTrash(note.getTrash());
	    noteDTO.setColour(note.getColour());
	    noteDTO.setLinks(note.getUrl());
	    
		return noteDTO;
	}

	@Override
	@Transactional
	public List<WebScrapping> webScrap(String url,long noteid){
		//System.out.println("aaaaa "+url);
		ArrayList<String> links = new ArrayList<String>();
		Notes notes=new Notes();
		notes.setId(noteid);
		List<WebScrapping> listOfwebs=new ArrayList<WebScrapping>();
		 
		String regex = "\\(?\\b(https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		while(m.find()) {
		String urlStr = m.group();
		if (urlStr.startsWith("(") && urlStr.endsWith(")"))
		{
		urlStr = urlStr.substring(1, urlStr.length() - 1);
		}
		links.add(urlStr);
		}
//		System.out.println("links array "+links);
		for(String s:links){
//			System.out.println("iterator "+s);
			URL url2;
			WebScrapping web=new WebScrapping();
					try {
						url2 = new URL(s);
						String host=url2.getHost();
						web.setDescription(host);
						Document doc = Jsoup.connect(s).get();
						String title=doc.title();
						web.setTitle(title);
						Element productLink = doc.select("a").first();
						String href = productLink.attr("abs:href");
						web.setImage(href);
					   web.setNote(notes);
						notesDAO.saveWeb(web);
						listOfwebs.add(web);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
		return listOfwebs;
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
			notesDTO1.setModifiedDate(note.getModifiedDate());
			notesDTO1.setProfile(note.getUserid().getProfile());
			notesDTO1.setLinks(note.getUrl());
			
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
			dto.setProfile(usr.getProfile());
			
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
    		
    		// System.out.println("removed the user "); 
    		 
    	 }
    	 
    	 
     }
//     note.setShareTo(listOfCOllaborator);
//     notesDAO.update(note);
     
     //System.out.println("user list "+listOfCOllaborator.toString()); 
     
		
	}

}
