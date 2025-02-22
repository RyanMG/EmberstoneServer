package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.*;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import com.emberstone.emberstone_tavern.repository.CampaignPersonJoinRepository;
import com.emberstone.emberstone_tavern.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;
    private final CampaignPersonJoinRepository campaignPersonJoinRepository;
    private final CampaignUtils campaignUtils;

    public CampaignService(
            PersonService personService,
            CampaignRepository campaignRepository,
            CampaignPersonJoinRepository campaignPersonJoinRepository,
            CampaignUtils campaignUtils
    ) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
        this.campaignPersonJoinRepository = campaignPersonJoinRepository;
        this.campaignUtils = campaignUtils;
    }

    public Set<CampaignOverviewModel> getActiveCampaignsForUser(String email) {
        Optional<PersonModel> user = personService.getActivePersonByEmail(email);
        if (user.isPresent()) {
            return campaignRepository.getAllActiveCampaignsForUser(user.get().getId());
        }
        return new HashSet<>();
    }

    public Set<CampaignOverviewModel> getCompletedCampaignsForUser(String email) {
        Optional<PersonModel> user = personService.getActivePersonByEmail(email);
        if (user.isPresent()) {
            return campaignRepository.getAllCompletedCampaignsForUser(user.get().getId());
        }
        return new HashSet<>();
    }

    public Optional<CampaignModel> getCampaignById(String email, UUID id) {
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
    }

    public HttpResponseModel<String> deleteUserFromCampaign(String email, UUID campaignId, UUID userId) {
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
    }

    public Optional<CampaignModel> createNewCampaign(String email, CampaignModel campaign) {
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
                CampaignPersonJoinModel resp = campaignPersonJoinRepository.save(newMember);
                return HttpResponseModel.success("User added to campaign", campaign.getId().toString());
            }

            return HttpResponseModel.error("User was not added to the campaign");

        } catch (Error e) {
            return HttpResponseModel.error("User was not added to the campaign");
        }
    }
}
