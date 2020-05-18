package com.eventapp.eventapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.eventapp.eventapp.entities.Event;

public interface EventRepository extends CrudRepository<Event, String> {
	
	Event findById(Long id);

}
