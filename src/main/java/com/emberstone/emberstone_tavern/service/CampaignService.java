package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.repository.*;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;
    private final CampaignPersonJoinRepository campaignPersonJoinRepository;
    private final CampaignSettingRepository campaignSettingRepository;
    private final CampaignInviteRepository campaignInviteRepository;
    private final CampaignUtils campaignUtils;
    private final PersonRepository personRepository;

    public CampaignService(
            PersonService personService,
            CampaignRepository campaignRepository,
            CampaignPersonJoinRepository campaignPersonJoinRepository,
            CampaignSettingRepository campaignSettingRepository,
            CampaignInviteRepository campaignInviteRepository,
            CampaignUtils campaignUtils,
            PersonRepository personRepository) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
        this.campaignPersonJoinRepository = campaignPersonJoinRepository;
        this.campaignSettingRepository = campaignSettingRepository;
        this.campaignInviteRepository = campaignInviteRepository;
        this.campaignUtils = campaignUtils;
        this.personRepository = personRepository;
    }

    public Set<CampaignOverviewModel> getActiveCampaignsForUser(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return campaignRepository.getAllActiveCampaignsForUser(user.get().getId());
            }
            return new HashSet<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get active campaigns for user: " + e.getMessage());
        }
    }

    public Set<CampaignOverviewModel> getCompletedCampaignsForUser(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return campaignRepository.getAllCompletedCampaignsForUser(user.get().getId());
            }
            return new HashSet<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get completed campaigns for user: " + e.getMessage());
        }
    }

    public Optional<CampaignModel> getCampaignById(String email, UUID id) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<CampaignModel> campaign = campaignRepository.findById(id);
                if (campaign.isPresent()) {
                    if (campaignUtils.userIsInCampaign(user.get().getId(), campaign.get())) {
                        return campaign;
                    }
                }
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get campaign by ID: " + e.getMessage());
        }
    }

    public HttpResponseModel<String> deleteUserFromCampaign(String email, UUID campaignId, UUID userId) {
        try {
            Optional<PersonModel> activeUser = personService.getActivePersonByEmail(email);
            Optional<CampaignModel> campaign = campaignRepository.findById(campaignId);
            if (activeUser.isPresent() && campaign.isPresent()) {
                if (campaign.get().getOwner().getId().equals(userId) || activeUser.get().getId().equals(userId)) {
                    Integer resp = campaignPersonJoinRepository.deletePlayerFromCampaign(userId, campaignId);
                    if (resp > 0) {
                        return HttpResponseModel.success("User was deleted", null);
                    }
                }

            }

            return HttpResponseModel.error("User was not deleted");

        } catch (Exception e) {
            throw new RuntimeException("Failed to remove user from campaign: " + e.getMessage());
        }
    }

    public Optional<CampaignModel> createNewCampaign(String email, CampaignModel campaign) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {

                campaign.setOwnerId(user.get().getId());
                campaign.setCampaignStatus(CampaignModel.CampaignStatus.ACTIVE);
                campaign.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                do {
                    campaign.setCampaignCode(campaignUtils.generateCampaignInviteCode());
                } while (campaignRepository.existsByCampaignCode(campaign.getCampaignCode()));

                CampaignModel newCampaign = campaignRepository.save(campaign);
                return Optional.of(newCampaign);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create new campaign: " + e.getMessage());
        }
    }

    public Optional<CampaignModel> updateCampaign(String email, CampaignModel updatedCampaign) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<CampaignModel> existingCampaignMatch = campaignRepository.findById(updatedCampaign.getId());
                if (existingCampaignMatch.isPresent()) {
                    CampaignModel campaign = existingCampaignMatch.get();
                    campaign.setTitle(updatedCampaign.getTitle());
                    campaign.setDescription(updatedCampaign.getDescription());
                    campaign.setIconLink(updatedCampaign.getIconLink());

                    CampaignModel updated = campaignRepository.save(campaign);
                    return Optional.of(updated);
                }

            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update campaign: " + e.getMessage());
        }
    }

    public HttpResponseModel<String> addUserToCampaign(String email, String campaignCode) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                CampaignModel campaign = campaignRepository.getByCampaignCode(campaignCode);
                if (campaign == null) {
                    return HttpResponseModel.error("No campaign found for this code");
                }

                if (campaignUtils.userIsInCampaign(user.get().getId(), campaign)) {
                    return HttpResponseModel.success("User is already in campaign", campaign.getId().toString());
                }

                CampaignPersonJoinModel newMember = new CampaignPersonJoinModel();
                newMember.setCampaign_id(campaign.getId());
                newMember.setPlayer_id(user.get().getId());

                campaignPersonJoinRepository.save(newMember);
                return HttpResponseModel.success("User added to campaign", campaign.getId().toString());
            }

            return HttpResponseModel.error("User was not added to the campaign");

        } catch (Error e) {
            return HttpResponseModel.error("User was not added to the campaign");
        }
    }

    public HttpResponseModel<String> inviteMemberByEmail(String ownerEmail, UUID id, String inviteeEmail) {
        try {
            Optional<CampaignModel> campaign = campaignRepository.findById(id);
            Optional<PersonModel> owner = personService.getActivePersonByEmail(ownerEmail);

            if (campaign.isPresent() && owner.isPresent()) {
                Optional<PersonModel> invitee = personRepository.findByEmail(inviteeEmail);
                if (invitee.isPresent()) {
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

    public List<CampaignPersonInvite> getMemberInvites(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                return campaignInviteRepository.getInvitesForPerson(user.get().getId());
            }
            return List.of((CampaignPersonInvite) null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get invites: " + e.getMessage());
        }
    }

    public List<CampaignSettingModel> getCampaignSettings() {
        try {
            return campaignSettingRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error getting campaign settings: " + e.getMessage());
        }
    }
}
