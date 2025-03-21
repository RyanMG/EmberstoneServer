package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.service.RosterService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/rosters")
public class RosterController {
    private final RosterService rosterService;
    public RosterController(RosterService rosterService) {
        this.rosterService = rosterService;
    }

    @GetMapping("")
    public Set<RosterModel> getAllUserRosters(Authentication authentication) {
        return rosterService.getAllUserRosters(authentication.getName());
    }

    @GetMapping("/{id}")
    public Optional<RosterModel> getRosterById(Authentication authentication, @PathVariable UUID id) {
        return rosterService.getRosterById(authentication.getName(), id);
    }

    @DeleteMapping("/{id}")
    public HttpResponseModel<UUID> deleteRoster(Authentication authentication, @PathVariable UUID id) {
        return rosterService.deleteRoster(authentication.getName(), id);
    }

    @GetMapping("/campaign/{campaignId}")
    public Optional<List<RosterModel>> getAllCampaignRosters(Authentication authentication, @PathVariable UUID campaignId) {
        return rosterService.getAllCampaignRosters(authentication.getName(), campaignId);
    }

    @PostMapping("")
    public HttpResponseModel<UUID> createUserCampaignRoster(Authentication authentication, @RequestBody RosterModel roster) {
        return rosterService.createUserCampaignRoster(authentication.getName(), roster);
    }

    @PutMapping("/{id}")
    public HttpResponseModel<RosterModel> updateRoster(Authentication authentication, @PathVariable UUID id, @RequestBody RosterModel roster) {
        return rosterService.updateRoster(authentication.getName(), id, roster);
    }

    @PostMapping("/regiments")
    public HttpResponseModel<RegimentModel> createNewRegiment(Authentication authentication, @RequestBody RegimentModel regiment) {
        return rosterService.createNewRegiment(authentication.getName(), regiment);
    }
    @DeleteMapping("/regiments/{regimentId}")
    public HttpResponseModel<Integer> createNewRegiment(Authentication authentication, @PathVariable Integer regimentId) {
        return rosterService.deleteRegiment(authentication.getName(), regimentId);
    }
}
