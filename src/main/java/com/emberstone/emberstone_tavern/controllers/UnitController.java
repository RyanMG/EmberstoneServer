package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.UnitModel;
import com.emberstone.emberstone_tavern.model.roster.UnitTypeModel;
import com.emberstone.emberstone_tavern.service.UnitService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @PostMapping("")
    public HttpResponseModel<UnitModel> createNewUnit(Authentication authentication, @RequestParam UUID rosterId, @RequestParam Integer regimentId, @RequestBody UnitModel unitModel) {
        return unitService.createNewUnit(authentication.getName(), rosterId, regimentId, unitModel);
    }
}
