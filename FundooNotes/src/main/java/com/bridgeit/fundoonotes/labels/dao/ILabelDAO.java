package com.bridgeit.fundoonotes.labels.dao;

import java.util.List;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.model.User;

public interface ILabelDAO {

	long save(Labels labels);
	Labels getLabelByName(String labelName);
	List<Labels> getAllLabels(User user);
	Labels getLabelsById(long labelId);
	boolean update(Labels labels);
	boolean delete(long labelId);
}
