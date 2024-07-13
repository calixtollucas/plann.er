package com.devruka.planner.controller;

import com.devruka.planner.model.activity.ActivityDTO;
import com.devruka.planner.model.activity.ActivityRequestPayload;
import com.devruka.planner.model.activity.ActivityResponse;
import com.devruka.planner.model.links.LinkCreateResponse;
import com.devruka.planner.model.links.LinkRequestPayload;
import com.devruka.planner.model.links.LinkResponse;
import com.devruka.planner.model.participant.ParticipantCreateResponse;
import com.devruka.planner.model.participant.ParticipantDTO;
import com.devruka.planner.model.participant.ParticipantRequestPayload;
import com.devruka.planner.model.trip.CreateTripResponseDTO;
import com.devruka.planner.model.trip.Trip;
import com.devruka.planner.model.trip.TripRequestPayload;
import com.devruka.planner.services.ActivityService;
import com.devruka.planner.services.LinkService;
import com.devruka.planner.services.ParticipantsService;
import com.devruka.planner.services.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    LinkService linkService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ParticipantsService participantsService;

    @Autowired
    TripService tripService;

    @PostMapping
    public ResponseEntity<CreateTripResponseDTO> createTrip(@RequestBody @Valid  TripRequestPayload registerDTO){
        @Valid
        Trip newTrip = new Trip(registerDTO);
            tripService.save(newTrip);
            participantsService.registerParticipantsToEvent(registerDTO.emails_to_invite(), newTrip);
            return ResponseEntity.ok(new CreateTripResponseDTO(newTrip.getId()));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable UUID tripId){
        Optional<Trip> trip = tripService.getTripById(tripId);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID tripId, @RequestBody TripRequestPayload payload){
        Optional<Trip> trip = tripService.getTripById(tripId);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setDestination(payload.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

            tripService.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID tripId){
        Optional<Trip> trip = tripService.getTripById(tripId);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            tripService.save(rawTrip);
            participantsService.triggerConfirmationEmail(rawTrip.getId());

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID tripId, @RequestBody @Valid ActivityRequestPayload payload){
        Optional<Trip> trip = tripService.getTripById(tripId);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = activityService.registerActivity(payload, rawTrip);
            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable UUID tripId){
        List<ActivityDTO> activitiesList = activityService.getAllActivitiesFromTripId(tripId);

        if(activitiesList != null){
            return ResponseEntity.ok(activitiesList);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID tripId, @RequestBody @Valid ParticipantRequestPayload payload){
        Optional<Trip> trip = tripService.getTripById(tripId);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            ParticipantCreateResponse response = participantsService.registerParticipantToEvent(payload.email() ,rawTrip);

            if(rawTrip.getIsConfirmed()){
                participantsService.triggerConfirmationEmailToParticipant(payload.email());
            }

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(@PathVariable UUID tripId){
        List<ParticipantDTO> participantsList = this.participantsService.getAllParticipantsFromTrip(tripId);

        if(participantsList != null){
            return ResponseEntity.ok(participantsList);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkCreateResponse> registerLink(@PathVariable UUID tripId, @RequestBody LinkRequestPayload payload){
        Optional<Trip> trip = this.tripService.getTripById(tripId);

        if(trip.isPresent()){
            Trip rawTrip = trip.get();

            LinkCreateResponse response = this.linkService.registerLink(payload, rawTrip);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(@PathVariable UUID tripId){
        List<LinkResponse> links = this.linkService.getAllLinkFromTripId(tripId);

        return ResponseEntity.ok(links);
    }
}
