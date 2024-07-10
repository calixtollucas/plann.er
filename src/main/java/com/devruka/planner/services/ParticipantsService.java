package com.devruka.planner.services;

import com.devruka.planner.model.participant.Participant;
import com.devruka.planner.model.participant.ParticipantCreateResponse;
import com.devruka.planner.model.participant.ParticipantDTO;
import com.devruka.planner.model.trip.Trip;
import com.devruka.planner.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantsService {

    @Autowired
    ParticipantRepository repository;

    public Optional<Participant> findById(UUID id){
        return repository.findById(id);
    }

    public void update(Participant participant){
        repository.save(participant);
    }

    public void save(Participant participant){
        Optional<Participant> participantOpt = this.findById(participant.getId());

        if(participantOpt.isEmpty()){
            repository.save(participant);
            return;
        }

        throw new RuntimeException("Error while creating a participant");
    }

    //cadastra todos os participantes na tabela de participantes
    public void registerParticipantsToEvent(List<String> participantsEmails, Trip trip){
        List<Participant> participants = participantsEmails.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        repository.saveAll(participants);
        System.out.println(participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip){
        Participant newParticipant = new Participant(email, trip);
        repository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    //envia o email para todos os participantes
    public void triggerConfirmationEmail(UUID eventId){
    }
    //envia o email de confirmacao para o participante especificado pelo ID
    public void triggerConfirmationEmailToParticipant(String email){
    }

    public List<ParticipantDTO> getAllParticipantsFromTrip(UUID tripId) {
        List<ParticipantDTO> participants = this.repository.findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantDTO(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getIsConfirmed()
                )).toList();

        return participants;
    }
}
