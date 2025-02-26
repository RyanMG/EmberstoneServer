package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.RosterModel;
import com.emberstone.emberstone_tavern.service.RosterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

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
    public Optional<RosterModel> getRosterById(Authentication authentication, @PathVariable Integer id) {
        return rosterService.getRosterById(authentication.getName(), id);
    }

    @GetMapping("/campaign/{campaignId}")
    public Optional<RosterModel> getUserCampaignRoster(Authentication authentication, @PathVariable UUID campaignId) {
        return rosterService.getUserCampaignRoster(authentication.getName(), campaignId);
    }
}
