package com.cnewbywa.events.service;

import java.util.UUID;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.cnewbywa.events.model.Event;

import io.smallrye.common.annotation.RunOnVirtualThread;

import com.cnewbywa.events.EventMessage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventsMessagingServiceImpl {
	
	private static final Logger LOG = Logger.getLogger(EventsMessagingServiceImpl.class);
	
	private static final String TOPIC = "events";
	
	@Inject
	EventsDBServiceImpl eventsDBService;
	
	@Incoming(TOPIC)
	@Retry(delay = 10, maxRetries = 3)
	@RunOnVirtualThread
	public void consumeEvent(EventMessage eventMessage) {
		LOG.info("Received event with message id: " + eventMessage.id());
		
		eventsDBService.storeEvent(createEventForStorage(eventMessage));
	}
	
	private Event createEventForStorage(EventMessage eventMessage) {
		return new Event(UUID.randomUUID().toString(), 
						 eventMessage.affectedId(), 
						 eventMessage.message(), 
						 eventMessage.applicationId(),
						 eventMessage.action(), 
						 eventMessage.creationTime());
	}
}
