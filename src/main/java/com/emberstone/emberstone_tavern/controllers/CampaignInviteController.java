package com.emberstone.emberstone_tavern.controllers;

import com.emberstone.emberstone_tavern.dto.CampaignInviteDTO;
import com.emberstone.emberstone_tavern.dto.EmailDTO;
import com.emberstone.emberstone_tavern.model.CampaignPersonInvite;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.service.CampaignInviteService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/campaigns/invite")
public class CampaignInviteController {
    private final CampaignInviteService campaignInviteService;

    public CampaignInviteController(CampaignInviteService campaignInviteService) {
        this.campaignInviteService = campaignInviteService;
    }

    @PostMapping("/{campaignId}")
    public HttpResponseModel<String> inviteMemberByEmail(Authentication authentication, @PathVariable UUID campaignId, @RequestBody EmailDTO email) {
        return campaignInviteService.inviteMemberByEmail(authentication.getName(), campaignId, email.getEmail());
    }

    @GetMapping("")
    public List<CampaignInviteDTO> getMemberInvites(Authentication authentication) {
        return campaignInviteService.getMemberInvites(authentication.getName());
    }

    @PutMapping("/{inviteId}")
    public HttpResponseModel<String> acceptCampaignInvite(Authentication authentication, @PathVariable Integer inviteId) {
        return campaignInviteService.acceptCampaignInvite(authentication.getName(), inviteId);
    }

    @DeleteMapping("/{inviteId}")
    public HttpResponseModel<String> rejectCampaignInvite(Authentication authentication, @PathVariable Integer inviteId) {
        return campaignInviteService.rejectCampaignInvite(authentication.getName(), inviteId);
    }
}
