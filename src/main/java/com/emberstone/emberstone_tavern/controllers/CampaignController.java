package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.service.CampaignInviteService;
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
    private final CampaignInviteService campaignInviteService;

    public CampaignController(CampaignService campaignService, CampaignInviteService campaignInviteService) {
        this.campaignService = campaignService;
        this.campaignInviteService = campaignInviteService;
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
        return campaignService.getUserCampaignById(authentication.getName(), id);
    }

    @DeleteMapping("/{campaignId}/users/{userId}")
    public HttpResponseModel<String> deleteUserFromCampaign(Authentication authentication, @PathVariable UUID campaignId, @PathVariable UUID userId) {
        return campaignService.deleteUserFromCampaign(authentication.getName(), campaignId, userId);
    }

    @PutMapping("/join/{campaignCode}")
    public HttpResponseModel<String> addUserToCampaign(Authentication authentication, @PathVariable String campaignCode) {
        return campaignInviteService.joinByCampaignCode(authentication.getName(), campaignCode);
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
