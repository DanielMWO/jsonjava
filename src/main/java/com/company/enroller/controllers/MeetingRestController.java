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
	
	//Pobieranie  wszytskich spotkań  GET /meetings
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	//Pobieranie spotkania GET /meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetinsById(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}	
	//Dodawanie spotkania POST /meetings/
	@RequestMapping(value="", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findByID(meeting.getId()) == null) {
			meetingService.createMeeting(meeting);
			return new ResponseEntity(meeting, HttpStatus.CREATED);
			}
		else {return new ResponseEntity("Meeting already exists", HttpStatus.CONFLICT);}
	}
	//Dodawanie uczetnika do spotkania  PUT /meetings/2/participants
	//Modify to accept login as JSON not TEXT 
	//Modifi so that participant cannot be added twice
	@RequestMapping(value="/{id}/participants/{login}", method = RequestMethod.PUT)
	public ResponseEntity<?> addParticipantToMeeting(
			@PathVariable("id") long id, 
			@PathVariable("login")  String participantLogin) {
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
	//Pobieranie uczestników sppotkania GET /meetings/2/participants
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
	
	//Usuwanie spotkań  DELETE /meetings/{id} 
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity("Meeting not found", HttpStatus.NOT_FOUND);
		}
		
		meetingService.delete(meeting);
		return new ResponseEntity("Meetining by id: " + meeting.getId() + " deleted", HttpStatus.OK);
	}	
	
	
	
	//Aktualizacja spotkań  PUT /meetings/{id}
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
		Meeting tmpMeeting = meetingService.findByID(id);
		if (tmpMeeting == null) {
			return new ResponseEntity("Meeting not found", HttpStatus.NOT_FOUND);
		}
		
		meetingService.update(id, meeting);
		return new ResponseEntity("Meetining by id: " + meeting.getId() + " updated", HttpStatus.OK);
	}	
	


	//Usuwanie uczersnika ze spotkania DELETE /meetings/{id}/participants/{login}
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeUserFormMeetig(
			@PathVariable("id") long id, 
			@PathVariable("login") String login
			) {
		Meeting tmpMeeting = meetingService.findByID(id);
		Participant participant = participantService.findByLogin(login);
		Collection<Participant> tmpParticipants = meetingService.getParticipants(id);
		
		if (tmpMeeting == null) {
		return new ResponseEntity("Meeting not found", HttpStatus.NOT_FOUND);
		
		}
		
		if (tmpParticipants.isEmpty() == true) {
			return new ResponseEntity("Participant not found in meeting", HttpStatus.NOT_FOUND);
		}
		
		meetingService.removeParticipant(id, participant);
		return new ResponseEntity("Patrticipant removed", HttpStatus.OK);
	}	
	
	
	//Sotriowanie listy spotkan po tytule GET/meetings?Sorted
	
	//Pobieranie  wszytskich spotkań  GET /meetings
	@RequestMapping(value = "/sorted", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingsSorted() {
		Collection<Meeting> meetings = meetingService.getAllSortedByTitle();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	//przesukiwanie listy spotkań po  tytule i opisie GET/meetig?incudeng dupa
	@RequestMapping(value = "/search={wanted}", method = RequestMethod.GET)
	public ResponseEntity<?> searchMeetings(@PathVariable("wanted") String wanted) {
		Collection<Meeting> meetings = meetingService.search(wanted);
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	//przeszukiwanie listy spotkań po zapisanym uczetsniku GET /meetings includes user
	@RequestMapping(value = "/particpantlogin/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> searchMeetingsByUserLogin(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant.equals(null)) {
			return new ResponseEntity("Participant not found in meeting", HttpStatus.NOT_FOUND);
		}
		
		Collection<Meeting> meetings = meetingService.searchParticipant(participant);
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	
	
}
