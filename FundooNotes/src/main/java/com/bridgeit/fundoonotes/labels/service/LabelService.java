package com.bridgeit.fundoonotes.labels.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgeit.fundoonotes.labels.dao.LabelDAO;
import com.bridgeit.fundoonotes.labels.model.LabelDTO;
import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.dao.UserDao;
import com.bridgeit.fundoonotes.user.model.User;
import com.bridgeit.fundoonotes.user.utility.JWT;

@Service
public class LabelService implements ILabelService{

	@Autowired
	UserDao userDao;
	
	@Autowired
	LabelDAO labelDAO;
	
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

}
