package com.eventapp.eventapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventapp.eventapp.entities.Event;
import com.eventapp.eventapp.entities.Guest;

public interface GuestRepository extends CrudRepository<Guest, String> {
	
	public Iterable<Guest> findByEvent(Event event);
	public Guest findByDocumentNumber(String documentNumber);

}
