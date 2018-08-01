package com.bridgeit.fundoonotes.labels.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgeit.fundoonotes.labels.model.Labels;
import com.bridgeit.fundoonotes.user.model.User;

@Repository
public class LabelDAO implements ILabelDAO {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public long save(Labels labels) {
		System.out.println("save label successfully");
		Session session = sessionFactory.openSession();
		long labelId = (Long) session.save(labels);
		return labelId;
	}

	@Override
	public Labels getLabelByName(String labelName) {
		@SuppressWarnings("deprecation")
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Labels.class).add(Restrictions.eq("labelName",labelName));
		Labels label= (Labels) criteria.uniqueResult();
		
		return label;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Labels> getAllLabels(User user) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Labels.class).add(Restrictions.eq("userid", user));
		List<Labels> labelList = criteria.list();
		
		return labelList;
	}
}
