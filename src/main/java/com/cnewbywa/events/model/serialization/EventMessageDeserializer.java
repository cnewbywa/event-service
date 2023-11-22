package com.cnewbywa.events.model.serialization;

import com.cnewbywa.events.EventMessage;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class EventMessageDeserializer extends ObjectMapperDeserializer<EventMessage> {

	public EventMessageDeserializer() {
		super(EventMessage.class);
	}
	
	public EventMessageDeserializer(Class<EventMessage> type) {
		super(type);
	}
}
