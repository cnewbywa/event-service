package com.cnewbywa.events.model;

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record EventResponse(String id, String affectedId, String message, String application, String action, Instant time) {
	
}
