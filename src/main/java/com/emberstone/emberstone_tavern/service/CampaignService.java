package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import com.emberstone.emberstone_tavern.model.CampaignOverviewModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;

    public CampaignService(PersonService personService, CampaignRepository campaignRepository) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
    }

    public Set<CampaignOverviewModel> getActiveCampaignsForUser(String email) {
        Optional<PersonModel> user = personService.getActivePersonByEmail(email);
        if (user.isPresent()) {
            return campaignRepository.getAllActiveCampaignsForUser(user.get().getId());
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
}
