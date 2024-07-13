package com.devruka.planner.model.activity;

import com.devruka.planner.exceptions.BusinessException;
import com.devruka.planner.model.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Activity(String title, String occursAt, Trip trip) {
        this.title = title;
        this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
        this.trip = trip;

        //check if activity's date time is in trip period
        if(
                !(
                        (this.occursAt.isAfter(trip.getStartsAt()) || this.occursAt.isEqual(trip.getStartsAt()))
                                && (this.occursAt.isBefore(trip.getEndsAt()) || this.occursAt.isEqual(trip.getEndsAt()))
                )
        ){
            throw new BusinessException(new IllegalArgumentException(), "the activity should be in trip period", HttpStatus.BAD_REQUEST);
        }

    }
}
