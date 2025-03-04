package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
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

    public UnitService(
            UnitRepository unitRepository,
            UnitTypeRepository unitTypeRepository,
            PathRepository pathRepository,
            PersonRepository personRepository,
            RosterRepository rosterRepository
    ) {
        this.unitRepository = unitRepository;
        this.unitTypeRepository = unitTypeRepository;
        this.pathRepository = pathRepository;
        this.personRepository = personRepository;
        this.rosterRepository = rosterRepository;
    }

    public HttpResponseModel<UnitModel> createNewUnit(String email, UUID rosterId, UnitModel newUnit) {
        try {
            Optional<PersonModel> user = personRepository.findByEmail(email);
            Optional<RosterModel> activeRoster = rosterRepository.findById(rosterId);
            if (user.isPresent() && activeRoster.isPresent()) {
                if (activeRoster.get().getPlayerId().equals(user.get().getId())) {
                    UnitModel savedUnit = unitRepository.save(newUnit);
                    return HttpResponseModel.success("Unit saved successfully", savedUnit);
                }
            }
            return HttpResponseModel.error("User not found");

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
