package com.devruka.planner.controller;

import com.devruka.planner.model.participant.Participant;
import com.devruka.planner.model.participant.ParticipantRequestPayload;
import com.devruka.planner.services.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    ParticipantsService participantsService;

    @PostMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(
            @PathVariable UUID participantId,
            @RequestBody ParticipantRequestPayload payload){
        Optional<Participant> participant = participantsService.findById(participantId);

        if(participant.isPresent()){
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());

            participantsService.update(rawParticipant);

            return ResponseEntity.ok(rawParticipant);
        }

        return ResponseEntity.notFound().build();
    }
}
