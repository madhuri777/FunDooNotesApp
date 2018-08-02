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

@SuppressWarnings({ "deprecation", "unchecked" })
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
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Labels.class).add(Restrictions.eq("labelName",labelName));
		Labels label= (Labels) criteria.uniqueResult();
		
		return label;
	}

	
	@Override
	public List<Labels> getAllLabels(User user) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Labels.class).add(Restrictions.eq("userid", user));
		List<Labels> labelList = criteria.list();
		
		return labelList;
	}

	@Override
	public Labels getLabelsById(long labelId) {
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Labels.class).add(Restrictions.eq("labelId", labelId));
		Labels labels=(Labels) criteria.uniqueResult();
		
		return labels;
	}

	@Override
	public boolean update(Labels labels) {
		
		sessionFactory.getCurrentSession().update(labels);
		
		return true;
	}

	@Override
	public boolean delete(long labelId) {
		
		System.out.println("id dao "+labelId);
		int result=sessionFactory.getCurrentSession().createQuery("delete from Labels where id=:LabelId").setParameter("LabelId",labelId).executeUpdate();
		
		System.out.println("dao "+result);
		return result==1?true:false;
	}
}
