package com.cnewbywa.events.resource;

import org.jboss.resteasy.reactive.ResponseStatus;

import com.cnewbywa.events.model.EventListResponse;
import com.cnewbywa.events.model.EventResponse;
import com.cnewbywa.events.service.EventsDBServiceImpl;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/events")
@RunOnVirtualThread
public class EventsResource {

	@Inject
	EventsDBServiceImpl eventsDBService;
	
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    public EventResponse getEvent(String id) {
        return eventsDBService.getEvent(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    public EventListResponse getEvents(
    		@QueryParam("application") String application,
    		@DefaultValue("0") @QueryParam("page_index") int pageIndex, 
    		@DefaultValue("100") @QueryParam("page_size") int pageSize, 
    		@DefaultValue("+time") @QueryParam("sort_by") String sortBy) {
    	
    	EventListResponse eventListResponse;
    	
    	if (application == null || application.isBlank()) {
    		eventListResponse = eventsDBService.getEvents(Page.of(pageIndex, pageSize), createSort(sortBy));
    	} else {
    		eventListResponse = eventsDBService.getEventsByApplication(application, Page.of(pageIndex, pageSize), createSort(sortBy));
    	}
    	
    	return eventListResponse;
    }
    
    /**
     * Create a Sort object from input
     * 
     * @param sortBy
     * @return Sort object
     */
    private Sort createSort(String sortBy) {
    	Sort sort = Sort.empty();
    	
    	if (sortBy == null || sortBy.isBlank()) {
    		return sort;
    	}
    	
    	String[] sortFields = sortBy.strip().split(",");
    	
    	for (String sortField : sortFields) {
			switch(sortField.substring(0, 1)) {
				case "-" -> sort.and(sortField.substring(1), Sort.Direction.Descending);
				case "+" -> sort.and(sortField.substring(1), Sort.Direction.Ascending);
				default -> sort.and(sortField);
			}
		}
    	
    	return sort;
    }
}
