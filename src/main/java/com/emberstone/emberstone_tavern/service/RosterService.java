package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import com.emberstone.emberstone_tavern.model.roster.RosterGeneralModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.repository.rosters.RegimentRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterGeneralRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RosterService {
    private final RosterRepository rosterRepository;
    private final RegimentRepository regimentRepository;
    private final PersonService personService;
    private final RosterGeneralRepository rosterGeneralRepository;

    private static final Logger log = LoggerFactory.getLogger(RosterService.class);

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

    @Transactional
    public Optional<RosterModel> getRosterById(String email, UUID id) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<RosterModel> roster = rosterRepository.findByRosterId(id);
                roster.ifPresent(rosterModel -> {

                    // If the user created a new regiment, but never added a hero to lead it, we should clean it out.
                    if (rosterModel.hasAnyEmptyRegiments()) {
                        Set<RegimentModel> regimentCopy = new HashSet<>(rosterModel.getRegiments());
                        regimentCopy.removeIf(child -> child.getUnits().isEmpty());
                        rosterModel.getRegiments().clear();
                        rosterModel.getRegiments().addAll(regimentCopy);
                        rosterRepository.save(rosterModel);
                    }

                });

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

    @Transactional
    public HttpResponseModel<UUID> createUserCampaignRoster(String email, RosterModel roster) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                RosterModel savedRoster = rosterRepository.saveAndFlush(roster);
                createNewRegiment(savedRoster.getId(), true, false);

                return HttpResponseModel.success("Roster created successfully", savedRoster.getId());
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

    public void updateOrCreateRosterGeneral(UUID rosterId, Integer generalId) {
        try {
            Optional<RosterGeneralModel> currentRoster = rosterGeneralRepository.getByRosterId(rosterId);
            if (currentRoster.isPresent()) {
                currentRoster.get().setGeneralId(generalId);
                rosterGeneralRepository.save(currentRoster.get());

            } else {
                RosterGeneralModel newRosterGeneral = new RosterGeneralModel();
                newRosterGeneral.setRosterId(rosterId);
                newRosterGeneral.setGeneralId(generalId);

                rosterGeneralRepository.save(newRosterGeneral);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get current roster general: " + e.getMessage());
        }
    }

    @Transactional
    public HttpResponseModel<RegimentModel> createNewRegiment(String email, RegimentModel regiment) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                RegimentModel savedRegiment = createNewRegiment(regiment.getRosterId(), regiment);
                return HttpResponseModel.success("Regiment created successfully", savedRegiment);
            }

            return HttpResponseModel.error("No user found to create regiment under", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new regiment: " + e.getMessage());
        }
    }

    @Transactional
    public HttpResponseModel<Integer> deleteRegiment(String email, Integer regimentId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            Optional<RegimentModel> regimentToDelete = regimentRepository.findById(regimentId);
            if (user.isPresent() && regimentToDelete.isPresent()) {
                Optional<RosterModel> parentRoster = rosterRepository.findByRosterId(regimentToDelete.get().getRosterId());
                if (parentRoster.isPresent() && parentRoster.get().getPlayerId().equals(user.get().getId())) {
                    regimentRepository.delete(regimentToDelete.get());
                    return HttpResponseModel.success("Regiment deleted", regimentToDelete.get().getId());
                }
            }

            return HttpResponseModel.error("Failed to delete regiment", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete regiment: " + e.getMessage());
        }
    }
    /**
     * Helper methods
     */
    // Create a new regiment from a regiment model provided from the client
    private RegimentModel createNewRegiment(UUID rosterId, RegimentModel regiment) {
        RegimentModel newRegiment = new RegimentModel();
        newRegiment.setRosterId(rosterId);
        newRegiment.setUnits(new HashSet<>());
        newRegiment.setRegimentName(regiment.getRegimentName());
        setRegimentNumber(rosterId, newRegiment);
        newRegiment.setIsGeneral(regiment.getIsGeneral());
        newRegiment.setIsAuxiliary(regiment.getIsAuxiliary());

        return regimentRepository.save(newRegiment);
    }

    // Create a new blank regiment with server provided values
    void createNewRegiment(UUID rosterId, boolean isGeneral, boolean isAuxiliary) {
        RegimentModel newRegiment = new RegimentModel();
        newRegiment.setRosterId(rosterId);
        newRegiment.setUnits(new HashSet<>());
        setRegimentNumber(rosterId, newRegiment);
        newRegiment.setIsGeneral(isGeneral);
        newRegiment.setIsAuxiliary(isAuxiliary);

        regimentRepository.save(newRegiment);
    }

    // Set the regiment's number as the next in the list
    private void setRegimentNumber(UUID rosterId, RegimentModel regiment) {
        Integer regimentCount = regimentRepository.getCountForRoster(rosterId);
        regiment.setRegimentNumber(regimentCount + 1);
    }
}
