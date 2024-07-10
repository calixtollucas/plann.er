package com.devruka.planner.services;

import com.devruka.planner.model.trip.Trip;
import com.devruka.planner.repositories.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {

    @Autowired
    ParticipantsService participantsService;

    @Autowired
    TripRepository repository;

    public void save(Trip trip){
        Trip newTrip = trip;
        repository.save(newTrip);
    }

    public Optional<Trip> getTripById(UUID tripId){
        Optional<Trip> trip = repository.findById(tripId);

        return trip;
    }
}
