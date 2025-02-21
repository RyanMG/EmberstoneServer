package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import com.emberstone.emberstone_tavern.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CampaignService {

    private final PersonService personService;
    private final CampaignRepository campaignRepository;

    public CampaignService(PersonService personService, CampaignRepository campaignRepository) {
        this.personService = personService;
        this.campaignRepository = campaignRepository;
    }

    public Set<CampaignModel> getCampaignsForUser(String email) {
        Optional<PersonModel> user = personService.getActivePersonByEmail(email);
        if (user.isPresent()) {
            return campaignRepository.getAllCampaignsForUser(user.get().getId());
        }
        return new HashSet<>();
    }
}
