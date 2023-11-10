package com.cnewbywa.events.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.quarkus.panache.common.Page;

@JsonSerialize
public record EventListResponse(List<EventResponse> events, Page currentPage, Page nextPage, Page previousPage, long totalAmount) {
	
}
