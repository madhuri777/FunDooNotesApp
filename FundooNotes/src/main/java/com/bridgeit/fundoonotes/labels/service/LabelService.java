package com.bridgeit.fundoonotes.labels.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.labels.dao.LabelDAO;
import com.bridgeit.fundoonotes.labels.model.LabelDTO;
import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.notes.dao.NotesDAO;
import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.notes.model.NotesDTO;
import com.bridgeit.fundoonotes.user.dao.UserDao;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class LabelService implements ILabelService{

	@Autowired
	UserDao userDao;
	
	@Autowired
	LabelDAO labelDAO;
	
	@Autowired
	NotesDAO noteDAO;
	
	@Transactional
	@Override
	public boolean createLabel(String token, LabelDTO label) {
		
		long id=JWT.parseJWT(token);
		
		User user=userDao.getUserById(id);
		
		String labelName=label.getLabelName();
		
		Labels label1=labelDAO.getLabelByName(labelName);
				
		if(label1==null) {
			
			Labels labels=new Labels();
			labels.setLabelName(label.getLabelName());
			labels.setUser(user);
			 
			labelDAO.save(labels);

			return true;
			
		}else {
			System.out.println("label already exist");
			return false;
		}
	}

	@Transactional
	@Override
	public List<LabelDTO> getAllLabels(String token) {
		
		List<LabelDTO> listLabelsDTO=new ArrayList<LabelDTO>();
		
		long userid=JWT.parseJWT(token);
		User user=userDao.getUserById(userid);
		
		List<Labels> listLabels=labelDAO.getAllLabels(user);
		
		for(Labels labels:listLabels) {
			
			LabelDTO labelDto=new LabelDTO();
			
			labelDto.setLabelId(labels.getLabelId());
			labelDto.setLabelName(labels.getLabelName());
			
			listLabelsDTO.add(labelDto);
			
		}
		
		return listLabelsDTO;
	}

	@Transactional
	@Override
	public boolean updateLabels(long labelid, String token, LabelDTO dto) {
		
		long userid=JWT.parseJWT(token);
		
		Labels labels=labelDAO.getLabelsById(labelid);
		
		long labelUserId=labels.getUser().getUserId();
		
		
		if(userid==labelUserId) {
			
			labels.setLabelName(dto.getLabelName());
			
			labelDAO.update(labels);
			
			return true;
		}
		
		return true;
	}

	@Transactional
	@Override
	public boolean deleteLabel(String token, long labelId) {
		
		boolean status=false;
		
		long userid=JWT.parseJWT(token);
		
		Labels labels=labelDAO.getLabelsById(labelId);
		
		long labelUserId=labels.getUser().getUserId();
		
		if(userid==labelUserId) {
			
			status=labelDAO.delete(labelId);
			
			return status;
			
		}
		
		return status;
	}

	@Transactional
	@Override
	public boolean addLabels(String token, NotesDTO noteDTO, long labelId) {
		
		long tokenuserid=JWT.parseJWT(token);
		
		Set<Labels> labelList=new HashSet<Labels>();
		Set<Notes> noteList=new HashSet<Notes>();
		
		long noteid=noteDTO.getNoteid();
		
		Notes note=noteDAO.getNoteById(noteid);
		
		System.out.println("note in add label "+note);
		
		long userid=note.getUserid().getUserId();
		
		if(tokenuserid==userid) {
		 
		Labels label=labelDAO.getLabelsById(labelId); 
		
		System.out.println("label in add label method "+label);
		noteList=label.getNote();
		noteList.add(note);
		label.setNote(noteList);
		 
		labelDAO.update(label);
		
		labelList=note.getLabel();
		System.out.println("labelList "+labelList);
		labelList.add(label);
		note.setLabel(labelList);
		
		noteDAO.update(note);
		
		
		
		return true;
	}
		return false;
	}

	@Transactional
	@Override
	public boolean removeLabels(String token, NotesDTO noteDTO, long labelId) {
		
		long tokenuserid=JWT.parseJWT(token);
		long noteid=noteDTO.getNoteid();
		Notes note=noteDAO.getNoteById(noteid);
		
		long userid=note.getUserid().getUserId();
		
		if(tokenuserid==userid) {
			
			Set<Labels> labels=note.getLabel();
			System.out.println("remove the labels "+labels);
			
			Labels label=labelDAO.getLabelsById(labelId); 
			
			labels.remove(label);
			note.setLabel(labels);
			noteDAO.update(note);
			
		
		   System.out.println("after rmoving labels "+labels);
		return true;
	}		
		return false;
	}	

}
