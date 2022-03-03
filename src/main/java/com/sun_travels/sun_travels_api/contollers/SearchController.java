package com.sun_travels.sun_travels_api.contollers;

import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityRequest;
import com.sun_travels.sun_travels_api.dtos.SearchAvailabilityResponse;
import com.sun_travels.sun_travels_api.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api.sun-travels.com/room-availability")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController( SearchService searchService ) { this.searchService = searchService; }

    @PostMapping()
    public ResponseEntity<List<SearchAvailabilityResponse>> searchAvailability( @RequestBody SearchAvailabilityRequest request ) {
        List<SearchAvailabilityResponse> results = searchService.searchAvailability( request );
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}