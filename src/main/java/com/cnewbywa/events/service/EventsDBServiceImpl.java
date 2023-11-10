package com.cnewbywa.events.service;

import java.util.List;

import org.jboss.logging.Logger;

import com.cnewbywa.events.model.Event;
import com.cnewbywa.events.model.EventListResponse;
import com.cnewbywa.events.model.EventResponse;
import com.cnewbywa.events.repository.EventsRepository;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EventsDBServiceImpl implements EventsDBService {
	
	private static final Logger LOG = Logger.getLogger(EventsDBServiceImpl.class);
	
	@Inject
	EventsRepository eventsRepository;
	
	@Override
	public EventResponse getEvent(String eventId) {
		return eventsRepository.findByEventId(eventId).map(this::createEventDto).orElseThrow(() -> new NotFoundException("Event not found"));
	}

	@Override
	public EventListResponse getEvents(Page page, Sort sort) {
		List<EventResponse> events = eventsRepository.findAll(sort).page(page).stream().map(this::createEventDto).toList();
				
		long totalAmount = eventsRepository.count();
		
		return new EventListResponse(events, page, page.next(), page.previous(), totalAmount);
	}
	
	@Override
	public EventListResponse getEventsByApplication(String application, Page page, Sort sort) {
		List<EventResponse> events = eventsRepository.find("application", sort, application).page(page).stream().map(this::createEventDto).toList();
		
		long totalAmount = eventsRepository.count("application", application);
		
		return new EventListResponse(events, page, page.next(), page.previous(), totalAmount);
	}
	
	@Override
	public void storeEvent(Event event) {
		LOG.info("Received event to store with id " + event.eventId);
		
		eventsRepository.persist(event);
		
		LOG.info("id: " + event.id);
	}
	
	private EventResponse createEventDto(Event event) {
		return new EventResponse(event.eventId, event.affectedId, event.message, event.application, event.action, event.time);
	}
}
