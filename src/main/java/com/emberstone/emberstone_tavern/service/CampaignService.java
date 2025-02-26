package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.repository.*;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.*;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;
    private final CampaignPersonJoinRepository campaignPersonJoinRepository;
    private final CampaignSettingRepository campaignSettingRepository;
    private final CampaignUtils campaignUtils;

    public CampaignService(
            PersonService personService,
            CampaignRepository campaignRepository,
            CampaignPersonJoinRepository campaignPersonJoinRepository,
            CampaignSettingRepository campaignSettingRepository,
            CampaignUtils campaignUtils
    ) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
        this.campaignPersonJoinRepository = campaignPersonJoinRepository;
        this.campaignSettingRepository = campaignSettingRepository;
        this.campaignUtils = campaignUtils;
    }

    /**
     * Gets all campaigns for a user which are currently active
     */
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
    /**
     * Gets all campaigns for a user which are currently marked as complete
     */
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
    /**
     * Gets a campaign by ID
     */
    public Optional<CampaignModel> getCampaignById(UUID id) {
        try {
            return campaignRepository.findById(id);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get campaign by ID: " + e.getMessage());
        }
    }
    /**
     * Gets a campaign by ID ONLY if the user is a member
     */
    public Optional<CampaignModel> getUserCampaignById(String email, UUID id) {
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
    /**
     * Remove a user from a campaign
     */
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
    /**
     * Create a new campaign
     */
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
    /**
     * Update an existing campaign
     */
    public Optional<CampaignModel> updateCampaign(String email, UUID campaignId, CampaignModel updatedCampaign) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<CampaignModel> existingCampaignMatch = campaignRepository.findById(campaignId);

                if (existingCampaignMatch.isPresent() && campaignUtils.userIsCampaignOwner(user.get().getId(), existingCampaignMatch.get().getOwnerId())) {
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
    /**
     * Allows the owner to delete a campaign
     */
    public HttpResponseModel<UUID> deleteCampaign(String email, UUID campaignId) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Optional<CampaignModel> existingCampaignMatch = campaignRepository.findById(campaignId);

                if (existingCampaignMatch.isPresent() && campaignUtils.userIsCampaignOwner(user.get().getId(), existingCampaignMatch.get().getOwnerId())) {
                    campaignRepository.deleteById(campaignId);
                    return HttpResponseModel.success("Campaign was deleted", campaignId);
                }
            }
            return HttpResponseModel.error("Error deleting campaign");
        } catch (Exception e) {
            return HttpResponseModel.error("Error deleting campaign: " + e.getMessage());
        }
    }
    /**
     * Add a user as a member of a campaign
     */
    public HttpResponseModel<String> addUserToCampaign(PersonModel user, CampaignModel campaign) {
        try {
            if (campaignUtils.userIsInCampaign(user.getId(), campaign)) {
                return HttpResponseModel.success("User is already in campaign", campaign.getId().toString());
            }

            CampaignPersonJoinModel newMember = new CampaignPersonJoinModel();
            newMember.setCampaign_id(campaign.getId());
            newMember.setPlayer_id(user.getId());

            campaignPersonJoinRepository.save(newMember);
            return HttpResponseModel.success("User added to campaign", campaign.getId().toString());

        } catch (Error e) {
            return HttpResponseModel.error("User was not added to the campaign");
        }
    }
    /**
     * Get all possible campaign settings
     */
    public List<CampaignSettingModel> getCampaignSettings() {
        try {
            return campaignSettingRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error getting campaign settings: " + e.getMessage());
        }
    }
}
