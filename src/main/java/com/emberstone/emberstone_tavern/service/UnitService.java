package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.RosterGeneralModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.model.roster.UnitModel;
import com.emberstone.emberstone_tavern.model.roster.UnitTypeModel;
import com.emberstone.emberstone_tavern.repository.PersonRepository;
import com.emberstone.emberstone_tavern.repository.rosters.RosterRepository;
import com.emberstone.emberstone_tavern.repository.units.PathRepository;
import com.emberstone.emberstone_tavern.repository.units.UnitRepository;
import com.emberstone.emberstone_tavern.repository.units.UnitTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UnitService {
    private final UnitRepository unitRepository;
    private final UnitTypeRepository unitTypeRepository;
    private final PathRepository pathRepository;
    private final PersonRepository personRepository;
    private final RosterRepository rosterRepository;
    private final RosterService rosterService;

    public UnitService(
            UnitRepository unitRepository,
            UnitTypeRepository unitTypeRepository,
            PathRepository pathRepository,
            PersonRepository personRepository,
            RosterRepository rosterRepository,
            RosterService rosterService) {
        this.unitRepository = unitRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.pathRepository = pathRepository;
        this.personRepository = personRepository;
        this.rosterRepository = rosterRepository;
        this.rosterService = rosterService;
    }

    public HttpResponseModel<UnitModel> createNewUnit(String email, UUID rosterId, UnitModel newUnit) {
        try {
            Optional<PersonModel> user = personRepository.findByEmail(email);
            Optional<RosterModel> activeRoster = rosterRepository.findById(rosterId);

            // Ensure the user making the request owns the roster being updated
            if (user.isPresent() && activeRoster.isPresent() && activeRoster.get().getPlayerId().equals(user.get().getId())) {
                if (newUnit.getIsGeneral()) {

                    // Ensure there is not an existing general
                    Optional<RosterGeneralModel> rosterGeneralJoin = rosterService.getRosterGeneralJoin(rosterId);
                    if (rosterGeneralJoin.isPresent()) {
                        Optional<UnitModel> general = unitRepository.findById(rosterGeneralJoin.get().getGeneralId());
                        if (general.isPresent()) {
                            return HttpResponseModel.error("There is an existing general present for this campaign", general.get());
                        }

                        UnitModel savedUnit = unitRepository.save(newUnit);
                        rosterService.updateRosterGeneral(rosterId, savedUnit.getId());
                        return HttpResponseModel.success("General saved successfully", savedUnit);

                    } else {
                        UnitModel savedUnit = unitRepository.save(newUnit);
                        rosterService.addGeneralToRoster(rosterId, savedUnit.getId());
                        return HttpResponseModel.success("General saved successfully", savedUnit);
                    }
                }

                UnitModel savedUnit = unitRepository.save(newUnit);
                return HttpResponseModel.success("Unit saved successfully", savedUnit);
            }
            return HttpResponseModel.error("User not found", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new unit: " + e.getMessage());
        }
    }

    public List<UnitTypeModel> getUnitTypes() {
        try {
            return unitTypeRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get unit types: " + e.getMessage());
        }
    }

    public List<PathModel> getUnitPaths(boolean isHero, Integer unitTypeId) {
        try {
            return pathRepository.getPathsByUnitType(isHero, unitTypeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get unit paths: " + e.getMessage());
        }
    }
}
