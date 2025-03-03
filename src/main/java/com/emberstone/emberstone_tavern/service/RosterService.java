package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.repository.rosters.RegimentRepository;
import com.emberstone.emberstone_tavern.repository.units.PathRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RosterService {
    private final RosterRepository rosterRepository;
    private final PathRepository pathRepository;
    private final RegimentRepository regimentRepository;
    private final PersonService personService;

    public RosterService(
            RosterRepository rosterRepository,
            PersonService personService,
            RegimentRepository regimentRepository,
            PathRepository pathRepository
    ) {
        this.personService = personService;
        this.rosterRepository = rosterRepository;
        this.regimentRepository = regimentRepository;
        this.pathRepository = pathRepository;
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
            return HttpResponseModel.error("Failed to delete roster");

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

            return HttpResponseModel.error("No user found to create roster under");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new campaign roster: " + e.getMessage());
        }
    }

    public List<PathModel> getPathsByUnitType(String email, boolean isHero, Integer unitTypeId) {
        try {
            return pathRepository.getPathsByUnitType(isHero, unitTypeId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get unit path options: " + e.getMessage());
        }
    }
}
