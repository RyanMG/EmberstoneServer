package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.model.campaign.CampaignGameModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.dto.CampaignOverviewDTO;
import com.emberstone.emberstone_tavern.model.campaign.CampaignSettingModel;
import com.emberstone.emberstone_tavern.service.CampaignGameService;
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
    private final CampaignGameService campaignGameService;

    public CampaignController(
            CampaignService campaignService,
            CampaignInviteService campaignInviteService,
            CampaignGameService campaignGameService
    ) {
        this.campaignService = campaignService;
        this.campaignInviteService = campaignInviteService;
        this.campaignGameService = campaignGameService;
    }

    @GetMapping("/active")
    public Set<CampaignOverviewDTO> getActiveCampaignsForUser(Authentication authentication) {
        return campaignService.getActiveCampaignsForUser(authentication.getName());
    }

    @GetMapping("/completed")
    public Set<CampaignOverviewDTO> getCompletedCampaignsForUser(Authentication authentication) {
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

    @PutMapping("/{campaignId}")
    public Optional<CampaignModel> updateCampaign(Authentication authentication, @PathVariable UUID campaignId, @RequestBody CampaignModel campaign) {
        return campaignService.updateCampaign(authentication.getName(), campaignId, campaign);
    }

    @DeleteMapping("/{campaignId}")
    public HttpResponseModel<UUID> deleteCampaign(Authentication authentication, @PathVariable UUID campaignId) {
        return campaignService.deleteCampaign(authentication.getName(), campaignId);
    }

    @GetMapping("/settings")
    public List<CampaignSettingModel> getCampaignSettings() {
        return campaignService.getCampaignSettings();
    }

    @GetMapping("/{campaignId}/games")
    public HttpResponseModel<List<CampaignGameModel>> getCampaignGames(Authentication authentication, @PathVariable UUID campaignId) {
        return campaignGameService.getGamesByCampaignId(authentication.getName(), campaignId);
    }

    @PostMapping("/{campaignId}/games")
    public HttpResponseModel<Integer> createNewCampaignGame(
            Authentication authentication,
            @PathVariable UUID campaignId,
            @RequestBody CampaignGameModel game
    ) {
        return campaignGameService.saveNewCampaignGame(authentication.getName(), campaignId, game);
    }

    @GetMapping("/{campaignId}/games/{gameId}")
    public HttpResponseModel<CampaignGameModel> getGameById(@PathVariable Integer gameId) {
        return campaignGameService.getGameById(gameId);
    }
}
