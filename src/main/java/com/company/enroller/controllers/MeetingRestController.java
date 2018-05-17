package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
	
	@Autowired
	MeetingService meetingService;
	@Autowired
	ParticipantService participantService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetinsById(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}	
	
	@RequestMapping(value="", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findByID(meeting.getId()) == null) {
			meetingService.createMeeting(meeting);
			return new ResponseEntity(meeting, HttpStatus.CREATED);
			}
		else {return new ResponseEntity("Meeting already exists", HttpStatus.CONFLICT);}
	}
	//Modify to accept login as JSON not TEXT 
	//Modifi so that participant cannot be added twice
	@RequestMapping(value="/{id}/participants", method = RequestMethod.PUT)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody String  participantLogin) {
		if (participantService.findByLogin(participantLogin)==null) 
				{return new ResponseEntity("Particibant by login: " + participantLogin + "does not exist", HttpStatus.NOT_ACCEPTABLE);
			}
		else if (meetingService.findByID(id)== null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		else {
			Participant participant = participantService.findByLogin(participantLogin);
			meetingService.addParticipant(id, participant);
			
			return new ResponseEntity(HttpStatus.OK);
		}
	}
	@RequestMapping(value="/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantsOfMeeting(@PathVariable("id") long id) {
		if (meetingService.findByID(id)== null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		else {
			Collection<Participant> participants = meetingService.getParticipants(id);
			
			return new ResponseEntity(participants, HttpStatus.OK);
		}
	}
	
}
