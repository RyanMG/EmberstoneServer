package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.dto.MemberDTO;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignGameModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.repository.campaign.CampaignGameRepository;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CampaignGameService {
    private final PersonService personService;
    private final CampaignService campaignService;
    private final CampaignUtils campaignUtils;

    private final CampaignGameRepository campaignGameRepository;

    public CampaignGameService(
            PersonService personService,
            CampaignService campaignService,
            CampaignUtils campaignUtils,
            CampaignGameRepository campaignGameRepository
    ) {
        this.personService = personService;
        this.campaignService = campaignService;
        this.campaignUtils = campaignUtils;
        this.campaignGameRepository = campaignGameRepository;
    }

    public HttpResponseModel<Integer> saveNewCampaignGame(String userEmail, UUID campaignId, CampaignGameModel game) {
        try {
            Optional<MemberDTO> gameReporter = personService.getPersonByEmail(userEmail);
            Optional<CampaignModel> campaign = campaignService.getCampaignById(campaignId);
            if (gameReporter.isPresent() && campaign.isPresent() && campaignUtils.userIsInCampaign(gameReporter.get().getId(), campaign.get())) {
                CampaignGameModel saved = campaignGameRepository.save(game);
                return HttpResponseModel.success("New game saved", saved.getId());
            }
            return HttpResponseModel.error("New game was not saved", null);
        } catch (Exception e) {
            return HttpResponseModel.error("New game was not saved", null);
        }
    }
}
