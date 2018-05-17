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


}



