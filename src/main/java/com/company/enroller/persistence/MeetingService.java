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
		
		return (Meeting) connector.getSession().get(Meeting.class, id);
	}
		

	public void addParticipant(long id, Participant participant) {
		Meeting meeting  = (Meeting) connector.getSession().get(Meeting.class, id);
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.addParticipant(participant);
		connector.getSession().save(meeting);
		transaction.commit();

	}

	public Collection<Participant> getParticipants(long id) {
		Meeting meeting  = (Meeting) connector.getSession().get(Meeting.class, id);
		Collection<Participant> meetingParticipants = meeting.getParticipants();
		return  meetingParticipants;
	}

	public void createMeeting(Meeting meeting) {
	Transaction tmpTransaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
    tmpTransaction.commit();
		
	}

	public void update(long id, Meeting meeting) {
		// TODO Auto-generated method stub
		Meeting tmpMeeting  = (Meeting) connector.getSession().get(Meeting.class, id);
		Transaction transaction = connector.getSession().beginTransaction();
		tmpMeeting.setDate(meeting.getDate());
		tmpMeeting.setTitle(meeting.getTitle());
		tmpMeeting.setDescription(meeting.getDescription());
		connector.getSession().save(tmpMeeting);
		transaction.commit();
		
	}

	public void delete(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
				
	}

	public void removeParticipant(long id, Participant participant) {
		// TODO Auto-generated method stub
		Meeting meeting  = (Meeting) connector.getSession().get(Meeting.class, id);
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.removeParticipant(participant);
		connector.getSession().save(meeting);
		transaction.commit();
		
	}

	public Collection<Meeting> getAllSortedByTitle() {
		String hql = "FROM Meeting m ORDER BY m.title";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
		
		
	}

	public Collection<Meeting> search(String wanted) {
		String hql = "FROM Meeting m WHERE m.title LIKE '%" + wanted + "%' OR m.description LIKE '%" +wanted+ "%'";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	
	}


}



