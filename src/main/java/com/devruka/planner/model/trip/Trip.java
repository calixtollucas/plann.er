package com.devruka.planner.model.trip;

import com.devruka.planner.exceptions.BusinessException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "destination cannot be empty")
    private String destination;

    @FutureOrPresent(message = "the start date should be present or future data")
    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @FutureOrPresent(message = "the end date should be present or future data")
    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name="is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @NotBlank(message = "owner name cannot be empty")
    @Column(name="owner_name", nullable = false)
    private String ownerName;

    @Email(message = "invalid email")
    @Column(name="owner_email", nullable = false)
    private String ownerEmail;

    public Trip(TripRequestPayload payload){
        this.destination = payload.destination();
        this.isConfirmed = false;
        this.ownerEmail = payload.owner_email();
        this.ownerName = payload.owner_name();

        try{
            this.startsAt = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
            this.endsAt = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e){
            throw new BusinessException(e, "Error while parsing the data string, the string should be in ISO_DATE_TIME", HttpStatus.BAD_REQUEST);
        }

        //date Future or Present validation
        if(this.startsAt.isBefore(LocalDateTime.now())){
            throw new BusinessException(new IllegalArgumentException(), "The start date should be after today's date", HttpStatus.BAD_REQUEST);
        }
        if(this.endsAt.isBefore(LocalDateTime.now())){
            throw new BusinessException(new IllegalArgumentException(), "The end date should be after today's date", HttpStatus.BAD_REQUEST);
        }

        //start is equals or before endsAt
        if(this.startsAt.isAfter(this.endsAt)){
            throw new BusinessException(new IllegalArgumentException(),
                    "The start date should be equals or before end date",
                    HttpStatus.BAD_REQUEST);
        }

    }
}
