package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		return connector.getSession().createCriteria(Participant.class).list();
	}

	public Participant findByLogin(String login) {
		
		return (Participant) connector.getSession().get(Participant.class, login); 
		
	}

	public void create(Participant participant) {
		
	Transaction tmpTransaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
    tmpTransaction.commit();
		
		
	}

	public void delete(Participant tmpParticipant) {
	
	Transaction tmpTransaction = connector.getSession().beginTransaction();
		connector.getSession().delete(tmpParticipant);
    tmpTransaction.commit();
		
	}

	public void update(Participant participant, String login) {
	
	Participant tmpParticipant = (Participant) connector.getSession().get(Participant.class, login); 
	
	Transaction tmpTransaction = connector.getSession().beginTransaction();
		tmpParticipant.setPassword(participant.getPassword());
		connector.getSession().save(tmpParticipant);
    tmpTransaction.commit();
		
		
	}

}
