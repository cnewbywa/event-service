package com.cnewbywa.events.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.cnewbywa.events.EventMessage;
import com.cnewbywa.events.model.Event;
import com.cnewbywa.events.repository.EventsRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class EventsMessagingServiceImplTest {
	
	@Inject
	EventsRepository eventsRepository;
	
	@Inject
	EventsMessagingServiceImpl eventsMessagingServiceImpl;
	
	@AfterEach
	void destroy() {
		eventsRepository.deleteAll();
	}
	
	@Test
	void testConsumeEvent() throws InterruptedException {
		String affectedId = UUID.randomUUID().toString();
		
		Instant creationTime = Instant.now();
		
		EventMessage eventMessage = new EventMessage(
				UUID.randomUUID().toString(), 
				affectedId, 
				"ADD", 
				"Message 1", 
				creationTime, 
				"test");
		
		eventsMessagingServiceImpl.consumeEvent(eventMessage);
		
		Event event = eventsRepository.find("application", "test").firstResult();
		
		assertNotNull(event);
		assertNotNull(event.id);
		assertEquals(affectedId, event.affectedId);
		assertEquals("ADD", event.action);
		assertEquals("Message 1", event.message);
		assertNotNull(event.time);
		assertEquals("test", event.application);
	}
}
