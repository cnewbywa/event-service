package com.cnewbywa.events.repository;

import java.util.Optional;

import com.cnewbywa.events.model.Event;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventsRepository implements PanacheMongoRepository<Event> {
	
	public Optional<Event> findByEventId(String eventId) {
		return find("eventId", eventId).firstResultOptional();
	}
}
