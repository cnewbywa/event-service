package com.cnewbywa.events.service;

import com.cnewbywa.events.model.Event;
import com.cnewbywa.events.model.EventListResponse;
import com.cnewbywa.events.model.EventResponse;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

public interface EventsDBService {

	public EventResponse getEvent(String eventId);
	
	public EventListResponse getEvents(Page page, Sort sort);
	
	public EventListResponse getEventsByApplication(String application, Page page, Sort sort);
	
	public void storeEvent(Event event);
}
