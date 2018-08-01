package com.bridgeit.fundoonotes.labels.service;

import java.util.List;

import com.bridgeit.fundoonotes.labels.model.LabelDTO;

public interface ILabelService {

	boolean createLabel(String token,LabelDTO label);
	List<LabelDTO> getAllLabels(String token);
}
