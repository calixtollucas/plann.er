package com.devruka.planner.services;

import com.devruka.planner.model.links.Link;
import com.devruka.planner.model.links.LinkCreateResponse;
import com.devruka.planner.model.links.LinkRequestPayload;
import com.devruka.planner.model.links.LinkResponse;
import com.devruka.planner.model.trip.Trip;
import com.devruka.planner.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    @Autowired
    LinkRepository repository;

    public LinkCreateResponse registerLink(LinkRequestPayload payload, Trip trip){
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.repository.save(newLink);

        return new LinkCreateResponse(newLink.getId());
    }

    public List<LinkResponse> getAllLinkFromTripId(UUID tripId){
        return this.repository.findByTripId(tripId).stream()
                .map(link -> new LinkResponse(
                        link.getId(),
                        link.getTitle(),
                        link.getUrl()
                )).toList();
    }
}
