package com.devruka.planner.model.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    public Trip(TripRequestPayload payload){
        this.destination = payload.destination();
        this.isConfirmed = false;
        this.ownerEmail = payload.owner_email();
        this.ownerName = payload.owner_name();
        this.startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name="is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(name="owner_name", nullable = false)
    private String ownerName;

    @Column(name="owner_email", nullable = false)
    private String ownerEmail;
}
