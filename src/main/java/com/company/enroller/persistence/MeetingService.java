package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		return connector.getSession().createCriteria(Meeting.class).list();
		//String hql = "FROM Meeting";
		//Query query = connector.getSession().createQuery(hql);
		//return query.list();
	}

	public Meeting findByID(long id) {
		// TODO Auto-generated method stub
		return null;
	}
		

	public void addParticipant() {
		// TODO Auto-generated method stub
		
	}



}
