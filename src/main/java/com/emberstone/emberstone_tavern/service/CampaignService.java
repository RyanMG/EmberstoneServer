package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.dto.CampaignOverviewDTO;
import com.emberstone.emberstone_tavern.model.campaign.CampaignPersonJoinModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignSettingModel;
import com.emberstone.emberstone_tavern.model.roster.RosterModel;
import com.emberstone.emberstone_tavern.repository.campaign.CampaignPersonJoinRepository;
import com.emberstone.emberstone_tavern.repository.campaign.CampaignRepository;
import com.emberstone.emberstone_tavern.repository.campaign.CampaignSettingRepository;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;
    private final CampaignPersonJoinRepository campaignPersonJoinRepository;
    private final CampaignSettingRepository campaignSettingRepository;
    private final CampaignUtils campaignUtils;
    private final RosterService rosterService;

    public CampaignService(
            PersonService personService,
            CampaignRepository campaignRepository,
            CampaignPersonJoinRepository campaignPersonJoinRepository,
            CampaignSettingRepository campaignSettingRepository,
            CampaignUtils campaignUtils,
            RosterService rosterService) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
        this.campaignPersonJoinRepository = campaignPersonJoinRepository;
        this.campaignSettingRepository = campaignSettingRepository;
        this.campaignUtils = campaignUtils;
        this.rosterService = rosterService;
    }

    /**
     * Gets all campaigns for a user which are currently active
     */
    public Set<CampaignOverviewDTO> getActiveCampaignsForUser(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Set<CampaignModel> campaignSet = campaignRepository.getAllActiveCampaignsForUser(user.get().getId());
                return getCampaignOverviewDTOS(campaignSet);
            }
            return new HashSet<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get active campaigns for user: " + e.getMessage());
        }
    }
    /**
     * Gets all campaigns for a user which are currently marked as complete
     */
    public Set<CampaignOverviewDTO> getCompletedCampaignsForUser(String email) {
        try {
            Optional<PersonModel> user = personService.getActivePersonByEmail(email);
            if (user.isPresent()) {
                Set<CampaignModel> campaignSet = campaignRepository.getAllCompletedCampaignsForUser(user.get().getId());
                return getCampaignOverviewDTOS(campaignSet);
            }
            return new HashSet<>();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get completed campaigns for user: " + e.getMessage());
        }
    }

    private Set<CampaignOverviewDTO> getCampaignOverviewDTOS(Set<CampaignModel> campaignSet) {
        return campaignSet.stream().map(campaign -> {
            CampaignOverviewDTO campaignOverview = new CampaignOverviewDTO();
            campaignOverview.setId(campaign.getId());
            campaignOverview.setTitle(campaign.getTitle());
            campaignOverview.setDescription(campaign.getDescription());
            campaignOverview.setIconLink(campaign.getIconLink());
            campaignOverview.setOwnerId(campaign.getOwnerId());
            campaignOverview.setCampaignStatus(campaign.getCampaignStatus());
            return campaignOverview;
        }).collect(Collectors.toSet());
    }

    /**
     * Gets a campaign by ID
     */
    public Optional<CampaignModel> getCampaignById(UUID id) {
        try {
            Optional<CampaignModel> campaign = campaignRepository.findById(id);
            campaign.ifPresent(campaignModel -> campaignModel.getMembers().forEach(member -> {
                Optional<RosterModel> userRoster = rosterService.getUserCampaignRoster(member.getEmail(), campaignModel.getId());
                member.setRoster(userRoster.orElse(null));
            }));

            Optional<RosterModel> ownerRoster = rosterService.getUserCampaignRoster(campaign.get().getOwner().getEmail(), campaign.get().getId());
            campaign.get().getOwner().setRoster(ownerRoster.orElse(null));
            return campaign;

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
                        campaign.get().getMembers().forEach(member -> {
                            Optional<RosterModel> userRoster = rosterService.getUserCampaignRoster(member.getEmail(), campaign.get().getId());
                            member.setRoster(userRoster.orElse(null));
                        });
                        Optional<RosterModel> ownerRoster = rosterService.getUserCampaignRoster(campaign.get().getOwner().getEmail(), campaign.get().getId());
                        campaign.get().getOwner().setRoster(ownerRoster.orElse(null));
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
                newCampaign.setOwner(user.get());
                newCampaign.setMembers(new HashSet<>());
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
