package com.bridgeit.fundoonotes.notes.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgeit.fundoonotes.notes.model.Notes;

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

}
