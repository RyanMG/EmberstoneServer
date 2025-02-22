package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import com.emberstone.emberstone_tavern.model.CampaignOverviewModel;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.repository.CampaignPersonJoinRepository;
import com.emberstone.emberstone_tavern.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;
    private final CampaignPersonJoinRepository campaignPersonJoinRepository;

    public CampaignService(PersonService personService, CampaignRepository campaignRepository, CampaignPersonJoinRepository campaignPersonJoinRepository) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
        this.campaignPersonJoinRepository = campaignPersonJoinRepository;
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
                UUID userId = user.get().getId();
                UUID campaignOwnerId = campaign.get().getOwner().getId();
                Set<UUID> memberIds = campaign.get().getMembers().stream().map(PersonModel::getId).collect(Collectors.toSet());
                if (userId.equals(campaignOwnerId) || memberIds.contains(campaignOwnerId)) {
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
                    return HttpResponseModel.success("User was deleted");
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

            CampaignModel newCampaign = campaignRepository.save(campaign);
            return Optional.of(newCampaign);
        }

        return Optional.empty();
    }
}
