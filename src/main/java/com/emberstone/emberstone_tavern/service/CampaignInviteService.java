package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.dto.CampaignInviteDTO;
import com.emberstone.emberstone_tavern.dto.MemberDTO;
import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.repository.CampaignInviteRepository;
import com.emberstone.emberstone_tavern.repository.CampaignRepository;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CampaignInviteService {
    private final CampaignRepository campaignRepository;
    private final CampaignInviteRepository campaignInviteRepository;
    private final PersonService personService;
    private final CampaignService campaignService;
    private final CampaignUtils campaignUtils;

    public CampaignInviteService(
            CampaignRepository campaignRepository,
            CampaignInviteRepository campaignInviteRepository,
            PersonService personService,
            CampaignService campaignService,
            CampaignUtils campaignUtils
    ) {
        this.campaignRepository = campaignRepository;
        this.campaignInviteRepository = campaignInviteRepository;
        this.personService = personService;
        this.campaignService = campaignService;
        this.campaignUtils = campaignUtils;
    }
    /**
     * Invite a member to a campaign by their email address
     */
    public HttpResponseModel<String> inviteMemberByEmail(String ownerEmail, UUID id, String inviteeEmail) {
        try {
            Optional<CampaignModel> campaign = campaignRepository.findById(id);
            Optional<PersonModel> owner = personService.getActivePersonByEmail(ownerEmail);

            if (campaign.isPresent() && owner.isPresent()) {
                Optional<PersonModel> invitee = personService.getActivePersonByEmail(inviteeEmail);
                if (invitee.isPresent() && !campaignUtils.userIsInCampaign(invitee.get().getId(), campaign.get())) {
                    CampaignPersonInvite invite = new CampaignPersonInvite();
                    invite.setCampaignId(campaign.get().getId());
                    invite.setPlayerId(invitee.get().getId());
                    invite.setOwnerId(owner.get().getId());
                    invite.setInviteDate(new Timestamp(System.currentTimeMillis()));

                    campaignInviteRepository.save(invite);
                    return HttpResponseModel.success("Invite sent", null);
                }
                return HttpResponseModel.error("No player registered with that email.");
            }

            return HttpResponseModel.error("No matching campaign found");

        } catch (Exception e) {
            return HttpResponseModel.error("User was not added to the campaign");
        }
    }
    /**
     * Get all campaign invites for a member
     */
    public List<CampaignInviteDTO> getMemberInvites(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                List<CampaignPersonInvite> invites = campaignInviteRepository.getInvitesForPerson(user.get().getId());
                return invites.stream().map(invite -> {
                    CampaignInviteDTO inviteDTO = new CampaignInviteDTO();

                    MemberDTO owner = new MemberDTO();
                    owner.setId(invite.getOwnerId());
                    owner.setFirstName(invite.getOwner().getFirstName());
                    owner.setLastName(invite.getOwner().getLastName());

                    MemberDTO player = new MemberDTO();
                    player.setId(invite.getPlayerId());
                    player.setFirstName(invite.getPlayer().getFirstName());
                    player.setLastName(invite.getPlayer().getLastName());

                    inviteDTO.setId(invite.getId());
                    inviteDTO.setOwner(owner);
                    inviteDTO.setPlayer(player);
                    inviteDTO.setCampaignOverview(invite.getCampaignOverview());
                    return inviteDTO;
                }).collect(Collectors.toList());
            }
            return new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get invites: " + e.getMessage());
        }
    }
    /**
     * Join a campaign by campaignCode
     */
    public HttpResponseModel<String> joinByCampaignCode(String email, String campaignCode) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                CampaignModel campaign = campaignRepository.getByCampaignCode(campaignCode);
                if (campaign == null) {
                    return HttpResponseModel.error("No campaign found for this code");
                }

                return campaignService.addUserToCampaign(user.get(), campaign);
            }
            return HttpResponseModel.error("No matching user found.");

        } catch (Exception e) {
            return HttpResponseModel.error("User was not added to the campaign");
        }
    }
    /**
     * Accept a campaign invite
     */
    public HttpResponseModel<String> acceptCampaignInvite (String email, Integer inviteId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<CampaignPersonInvite> invite = campaignInviteRepository.findById(inviteId);
                if (invite.isPresent()) {
                    Optional<CampaignModel> campaign = campaignService.getCampaignById(invite.get().getCampaignId());
                    if (campaign.isPresent()) {
                        HttpResponseModel<String> resp =  campaignService.addUserToCampaign(user.get(), campaign.get());
                        campaignInviteRepository.deleteById(inviteId);
                        return resp;
                    }
                }
            }

            return HttpResponseModel.error("Failed to accept campaign invite.");

        } catch (Error e) {
            return HttpResponseModel.error("Failed to accept campaign invite");
        }
    }
    /**
     * Reject (delete) a campaign invite
     */
    public HttpResponseModel<String> rejectCampaignInvite (String email, Integer inviteId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                campaignInviteRepository.deleteById(inviteId);
                return HttpResponseModel.success("Invite rejected", null);
            }
            return HttpResponseModel.success("Failed to delete invite", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete invite: " + e.getMessage());
        }
    }
}
