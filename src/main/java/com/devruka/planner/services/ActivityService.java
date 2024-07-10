package com.devruka.planner.services;

import com.devruka.planner.model.activity.Activity;
import com.devruka.planner.model.activity.ActivityDTO;
import com.devruka.planner.model.activity.ActivityRequestPayload;
import com.devruka.planner.model.activity.ActivityResponse;
import com.devruka.planner.model.trip.Trip;
import com.devruka.planner.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    ActivityRepository repository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip){
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityDTO> getAllActivitiesFromTripId(UUID tripId){
        return repository.findByTripId(tripId).stream()
                .map(activity -> new ActivityDTO(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getOccursAt()
                )).toList();
    }
}
