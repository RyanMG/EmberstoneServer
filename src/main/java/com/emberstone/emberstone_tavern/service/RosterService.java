package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import com.emberstone.emberstone_tavern.model.roster.RosterGeneralModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.model.roster.UnitModel;
import com.emberstone.emberstone_tavern.repository.rosters.RegimentRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterGeneralRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RosterService {
    private final RosterRepository rosterRepository;
    private final RegimentRepository regimentRepository;
    private final PersonService personService;
    private final RosterGeneralRepository rosterGeneralRepository;

    public RosterService(
            RosterRepository rosterRepository,
            PersonService personService,
            RegimentRepository regimentRepository,
            RosterGeneralRepository rosterGeneralRepository
    ) {
        this.personService = personService;
        this.rosterRepository = rosterRepository;
        this.regimentRepository = regimentRepository;
        this.rosterGeneralRepository = rosterGeneralRepository;
    }

    public Optional<RosterModel> getRosterById(String email, UUID id) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<RosterModel> roster = rosterRepository.findByRosterId(id);

                return roster;
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

    public HttpResponseModel<UUID> deleteRoster(String email, UUID rosterId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<RosterModel> rosterToDelete = rosterRepository.findByRosterId(rosterId);
                if (rosterToDelete.isPresent() && rosterToDelete.get().getPlayerId().equals(user.get().getId())) {
                    rosterRepository.delete(rosterToDelete.get());

                    return HttpResponseModel.success("Roster deleted", rosterToDelete.get().getId());
                }
            }
            return HttpResponseModel.error("Failed to delete roster", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete roster: " + e.getMessage());
        }
    }

    public HttpResponseModel<RosterModel> createUserCampaignRoster(String email, RosterModel roster) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                RosterModel savedRoster = rosterRepository.save(roster);
                RegimentModel generalsRegiment = new RegimentModel();
                generalsRegiment.setRosterId(savedRoster.getId());
                generalsRegiment.setUnits(new HashSet<>());

                RegimentModel campaignGeneralsRegiment = regimentRepository.save(generalsRegiment);
                savedRoster.setRegiments(Set.of(campaignGeneralsRegiment));

                return HttpResponseModel.success("Roster created successfully", savedRoster);
            }

            return HttpResponseModel.error("No user found to create roster under", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new campaign roster: " + e.getMessage());
        }
    }

    public HttpResponseModel<RosterModel> updateRoster(String email, UUID rosterId, RosterModel updatedRoster) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            Optional<RosterModel> currentRoster = rosterRepository.findById(rosterId);
            if (user.isPresent() && currentRoster.isPresent() &&  currentRoster.get().getPlayerId().equals(user.get().getId())) {
                 RosterModel currentRosterModel = currentRoster.get();
                 currentRosterModel.setName(updatedRoster.getName());
                 currentRosterModel.setGrandAllianceId(updatedRoster.getGrandAllianceId());
                 currentRosterModel.setFactionId(updatedRoster.getFactionId());

                 RosterModel savedRoster = rosterRepository.save(currentRosterModel);
                 return HttpResponseModel.success("Roster created successfully", savedRoster);
            }

            return HttpResponseModel.error("Unable to edit roster", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new campaign roster: " + e.getMessage());
        }
    }

    public Optional<RosterGeneralModel> getRosterGeneralJoin(UUID rosterId) {
        try {
            return rosterGeneralRepository.getByRosterId(rosterId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get current roster general: " + e.getMessage());
        }
    }

    public RosterGeneralModel addGeneralToRoster(UUID rosterId, Integer generalId) {
        try {
            RosterGeneralModel newRosterGeneral = new RosterGeneralModel();
            newRosterGeneral.setRosterId(rosterId);
            newRosterGeneral.setGeneralId(generalId);

            rosterGeneralRepository.save(newRosterGeneral);
            return newRosterGeneral;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get current roster general: " + e.getMessage());
        }
    }

    public Optional<RosterGeneralModel> updateRosterGeneral(UUID rosterId, Integer generalId) {
        try {
            Optional<RosterGeneralModel> currentRoster = rosterGeneralRepository.getByRosterId(rosterId);
            if (currentRoster.isPresent()) {
                currentRoster.get().setGeneralId(generalId);
                rosterGeneralRepository.save(currentRoster.get());
                return currentRoster;
            }

            return currentRoster;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get current roster general: " + e.getMessage());
        }
    }
}
