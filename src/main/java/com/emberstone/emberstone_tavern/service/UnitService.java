package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.UnitTypeModel;
import com.emberstone.emberstone_tavern.repository.units.PathRepository;
import com.emberstone.emberstone_tavern.repository.units.UnitTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {
    private final UnitTypeRepository unitTypeRepository;
    private final PathRepository pathRepository;

    public UnitService(UnitTypeRepository unitTypeRepository,  PathRepository pathRepository) {
        this.unitTypeRepository = unitTypeRepository;
        this.pathRepository = pathRepository;
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
