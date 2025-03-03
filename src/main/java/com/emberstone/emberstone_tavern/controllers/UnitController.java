package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.UnitTypeModel;
import com.emberstone.emberstone_tavern.service.UnitService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/units")
public class UnitController {
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/unitTypes")
    public List<UnitTypeModel> getUnitTypes(Authentication authentication) {
        return unitService.getUnitTypes();
    }

    @GetMapping("/paths")
    public List<PathModel> getUnitPaths(Authentication authentication, @RequestParam boolean isHero, @RequestParam Integer unitTypeId) {
        return unitService.getUnitPaths(isHero, unitTypeId);
    }
}
