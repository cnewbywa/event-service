package com.cnewbywa.events.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

import com.cnewbywa.events.test.resources.CustomMongoResource;

@QuarkusIntegrationTest
@QuarkusTestResource(CustomMongoResource.class)
class EventsResourceIntegrationTest {

	@Test
	void testGetEvent() {
		given()
	    	.when()
	    		.get("/events/922b2539-696f-4de9-a2e2-eb373c0bf8a0")
	    	.then()
	    		.statusCode(200)
	    		.header("content-type", "application/json;charset=UTF-8")
	    		.body("id", equalTo("922b2539-696f-4de9-a2e2-eb373c0bf8a0"))
	    		.body("message", equalTo("Event message 1"))
	    		.body("action", equalTo("ADD"));
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
        		.body("events.id", hasItems("922b2539-696f-4de9-a2e2-eb373c0bf8a0", "02efa2d2-6e26-465e-be94-61f9ffda1f27", "ce432365-0642-4f52-a205-b93b47276345"))
        		.body("events.message", hasItems("Event message 1", "Event message 2", "Event message 3"))
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
        		.body("events.id", hasItems("922b2539-696f-4de9-a2e2-eb373c0bf8a0", "02efa2d2-6e26-465e-be94-61f9ffda1f27"))
        		.body("events.message", hasItems("Event message 1", "Event message 2"))
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
        		.body("events.id", hasItems("02efa2d2-6e26-465e-be94-61f9ffda1f27", "ce432365-0642-4f52-a205-b93b47276345"))
        		.body("events.message", hasItems("Event message 2", "Event message 3"))
        		.body("events.action", hasItems("MODIFY", "ADD"))
        		.body("currentPage.index", equalTo(0))
        		.body("currentPage.size", equalTo(2))
        		.body("nextPage.index", equalTo(1))
        		.body("nextPage.size", equalTo(2))
        		.body("previousPage.index", equalTo(0))
        		.body("previousPage.size", equalTo(2));
	}
}
