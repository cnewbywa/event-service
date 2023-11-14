package com.cnewbywa.events.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.equalTo;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cnewbywa.events.model.Event;
import com.cnewbywa.events.repository.EventsRepository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class EventsResourceTest {

	@Inject
	EventsRepository eventsRepository;
	
	private String eventId1 = UUID.randomUUID().toString();
	private String eventId2 = UUID.randomUUID().toString();
	private String eventId3 = UUID.randomUUID().toString();
	
	@BeforeEach
	void setup() {
		eventsRepository.persist(new Event(eventId1, UUID.randomUUID().toString(), "message 1", "test", "ADD", Instant.now()));
		eventsRepository.persist(new Event(eventId2, UUID.randomUUID().toString(), "message 2", "test", "MODIFY", Instant.now()));
		eventsRepository.persist(new Event(eventId3, UUID.randomUUID().toString(), "message 3", "test2", "ADD", Instant.now()));
	}
	
	@AfterEach
	void destroy() {
		eventsRepository.deleteAll();
	}
	
	@Test
	void testGetEvent() {
		given()
        	.when()
        		.get("/events/" + eventId1)
        	.then()
        		.statusCode(200)
        		.header("content-type", "application/json;charset=UTF-8")
        		.body("id", equalTo(eventId1))
        		.body("message", equalTo("message 1"))
        		.body("action", equalTo("ADD")).log();
	}
	
	@Test
	void testGetEvent_NotFound() {
		given()
	    	.when()
	    		.get("/events/notvalid")
	    	.then()
	    		.statusCode(404);
	}
	
	@Test
	void testGetEvents() {
		given()
    		.when()
    			.get("/events")
    		.then()
        		.statusCode(200)
        		.header("content-type", "application/json;charset=UTF-8")
        		.body("totalAmount", equalTo(3))
        		.body("events.size()", equalTo(3))
        		.body("events.id", hasItems(eventId1, eventId2, eventId3))
        		.body("events.message", hasItems("message 1", "message 2", "message 3"))
        		.body("events.action", hasItems("ADD", "MODIFY", "ADD"))
        		.body("currentPage.index", equalTo(0))
        		.body("currentPage.size", equalTo(100))
        		.body("nextPage.index", equalTo(1))
        		.body("nextPage.size", equalTo(100))
        		.body("previousPage.index", equalTo(0))
        		.body("previousPage.size", equalTo(100));
	}
	
	@Test
	void testGetEvents_WithApplication() {
		given()
    		.when()
    			.get("/events?application=test")
    		.then()
        		.statusCode(200)
        		.header("content-type", "application/json;charset=UTF-8")
        		.body("totalAmount", equalTo(2))
        		.body("events.size()", equalTo(2))
        		.body("events.id", hasItems(eventId1, eventId2))
        		.body("events.message", hasItems("message 1", "message 2"))
        		.body("events.action", hasItems("ADD", "MODIFY"))
        		.body("currentPage.index", equalTo(0))
        		.body("currentPage.size", equalTo(100))
        		.body("nextPage.index", equalTo(1))
        		.body("nextPage.size", equalTo(100))
        		.body("previousPage.index", equalTo(0))
        		.body("previousPage.size", equalTo(100));
	}
	
	@Test
	void testGetEvents_WithPaginationAndSorting() {
		given()
    		.when()
    			.get("/events?page_index=0&page_size=2&sort_by=-message")
    		.then()
        		.statusCode(200)
        		.header("content-type", "application/json;charset=UTF-8")
        		.body("totalAmount", equalTo(3))
        		.body("events.size()", equalTo(2))
        		.body("events.id", hasItems(eventId2, eventId3))
        		.body("events.message", hasItems("message 2", "message 3"))
        		.body("events.action", hasItems("MODIFY", "ADD"))
        		.body("currentPage.index", equalTo(0))
        		.body("currentPage.size", equalTo(2))
        		.body("nextPage.index", equalTo(1))
        		.body("nextPage.size", equalTo(2))
        		.body("previousPage.index", equalTo(0))
        		.body("previousPage.size", equalTo(2));
	}
}
