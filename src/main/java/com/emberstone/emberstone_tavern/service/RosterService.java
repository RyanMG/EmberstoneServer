package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.repository.RosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RosterService {
    private final RosterRepository rosterRepository;
    private final PersonService personService;

    public RosterService(RosterRepository rosterRepository, PersonService personService) {
        this.personService = personService;
        this.rosterRepository = rosterRepository;
    }

    public Optional<RosterModel> getRosterById(String email, UUID id) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return rosterRepository.findByRosterId(id);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get roster: " + e.getMessage());
        }
    }

    public Set<RosterModel> getAllUserRosters(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return rosterRepository.getAllByPlayerId(user.get().getId());
            }
            return Set.of();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all rosters for user: " + e.getMessage());
        }
    }

    public Optional<List<RosterModel>> getAllCampaignRosters(String email, UUID campaignId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return rosterRepository.getAllRostersByCampaignId(campaignId);
            }
            return Optional.empty();
        } catch(Exception e) {
            throw new RuntimeException("Failed to get all rosters for campaign: " + e.getMessage());
        }
    }

    public Optional<RosterModel> getUserCampaignRoster(String email, UUID campaignId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return rosterRepository.getRosterByPlayerIdAndCampaignId(user.get().getId(), campaignId);
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get campaign rosters for user: " + e.getMessage());
        }
    }

    public HttpResponseModel<RosterModel> createUserCampaignRoster(String email, RosterModel roster) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                RosterModel savedRoster = rosterRepository.save(roster);
                return HttpResponseModel.success("Roster created successfully", savedRoster);
            }

            return HttpResponseModel.error("No user found to create roster under");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new campaign roster: " + e.getMessage());
        }
    }
}
