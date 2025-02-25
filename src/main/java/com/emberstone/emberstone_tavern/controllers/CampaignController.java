package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import com.emberstone.emberstone_tavern.model.CampaignOverviewModel;
import com.emberstone.emberstone_tavern.model.CampaignSettingModel;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.service.CampaignService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/completed")
    public Set<CampaignOverviewModel> getCompletedCampaignsForUser(Authentication authentication) {
        return campaignService.getCompletedCampaignsForUser(authentication.getName());
    }

    @GetMapping("{id}")
    public Optional<CampaignModel> getCampaignById(Authentication authentication, @PathVariable UUID id) {
        return campaignService.getCampaignById(authentication.getName(), id);
    }

    @DeleteMapping("/{campaignId}/users/{userId}")
    public HttpResponseModel<String> deleteUserFromCampaign(Authentication authentication, @PathVariable UUID campaignId, @PathVariable UUID userId) {
        return campaignService.deleteUserFromCampaign(authentication.getName(), campaignId, userId);
    }

    @PutMapping("/join/{campaignCode}")
    public HttpResponseModel<String> addUserToCampaign(Authentication authentication, @PathVariable String campaignCode) {
        return campaignService.addUserToCampaign(authentication.getName(), campaignCode);
    }

    @PostMapping("")
    public Optional<CampaignModel> createNewCampaign(Authentication authentication, @RequestBody CampaignModel campaign) {
        return campaignService.createNewCampaign(authentication.getName(), campaign);
    }

    @PutMapping("/{id}")
    public Optional<CampaignModel> updateCampaign(Authentication authentication, @RequestBody CampaignModel campaign) {
        return campaignService.updateCampaign(authentication.getName(), campaign);
    }

    @GetMapping("/settings")
    public List<CampaignSettingModel> getCampaignSettings() {
        return campaignService.getCampaignSettings();
    }
}
