package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import com.emberstone.emberstone_tavern.model.CampaignOverviewModel;
import com.emberstone.emberstone_tavern.service.CampaignService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping("/active")
    public Set<CampaignOverviewModel> getActiveCampaignsForUser(Authentication authentication) {
        return campaignService.getActiveCampaignsForUser(authentication.getName());
    }
    @GetMapping("{id}")
    public Optional<CampaignModel> getCampaignById(Authentication authentication, @PathVariable UUID id) {
        return campaignService.getCampaignById(authentication.getName(), id);
    }
}
