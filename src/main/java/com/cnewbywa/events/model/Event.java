package com.cnewbywa.events.model;

import java.time.Instant;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(database = "events", collection = "event")
public class Event extends PanacheMongoEntity {
	
	public String eventId;
	public String affectedId;
	public String message;
	public String application;
	public String action;
	public Instant time;
	
	public Event() {
		super();
	}
	
	public Event(String eventId, String affectedId, String message, String application, String action, Instant time) {
		super();
		this.eventId = eventId;
		this.affectedId = affectedId;
		this.message = message;
		this.application = application;
		this.action = action;
		this.time = time;
	}
}
