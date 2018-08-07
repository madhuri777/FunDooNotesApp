package com.bridgeit.fundoonotes.notes.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgeit.fundoonotes.notes.model.Notes;
import com.bridgeit.fundoonotes.user.exception.DataBaseException;
import com.bridgeit.fundoonotes.user.model.User;

@Repository
public class NotesDAO implements INotesDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public long save(Notes notes) {
		System.out.println("save naote successfully");
		Session session = sessionFactory.openSession();
		long noteId = (Long) session.save(notes);
		return noteId;
	}

	@SuppressWarnings({  "unchecked", "rawtypes" })
	@Override
	public List<Notes> getAllNotes(User user) {
		 System.out.println("in notes dao method ");
		
			
			String hql="from Notes where userid=:userid";
			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("userid",user);
			List noteList = query.list();
			System.out.println("dao "+noteList); 
			
			
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Notes.class).add(Restrictions.eq("userid", user));
//		List<Notes> noteList = criteria.list();
//		
//		System.out.println("notes dao "+noteList);
		
		return noteList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Notes getNoteById(long noteid)throws DataBaseException {
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Notes.class).add(Restrictions.eq("id",noteid));
		Notes notes= (Notes) criteria.uniqueResult();
		
		if(notes!=null) {
			return notes;
		}else
			throw new DataBaseException("Exception: NullPointer Exception, Note does not exists");
	}

	@Override
	public boolean update(Notes notes) {
		
		sessionFactory.getCurrentSession().update(notes);
		
		return false;
	}

	@Override
	public boolean deletNoteById(long id)throws DataBaseException {
		System.out.println("note id "+id);
		int result=sessionFactory.getCurrentSession().createQuery("delete from Notes where id=:id").setParameter("id",id).executeUpdate();
		System.out.println(result);
		if(result==1) {
			return true;
		}else
			throw new DataBaseException("Exception: Row not deleted");
	}

}
