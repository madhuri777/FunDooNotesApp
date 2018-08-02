package com.bridgeit.fundoonotes.labels.service;

import java.util.List;

import com.bridgeit.fundoonotes.labels.model.LabelDTO;

public interface ILabelService {

	boolean createLabel(String token,LabelDTO label);
	List<LabelDTO> getAllLabels(String token);
	boolean updateLabels(long labelid,String token,LabelDTO dto);
	boolean deleteLabel(String token,long labelId);
}
