package com.emberstone.emberstone_tavern.service;

import com.emberstone.emberstone_tavern.dto.MemberDTO;
import com.emberstone.emberstone_tavern.model.HttpResponseModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignGameModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.repository.campaign.CampaignGameRepository;
import com.emberstone.emberstone_tavern.util.CampaignUtils;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public HttpResponseModel<List<CampaignGameModel>> getGamesByCampaignId(String email, UUID campaignId) {
        try {
            Optional<MemberDTO> user = personService.getPersonByEmail(email);
            Optional<CampaignModel> campaign = campaignService.getCampaignById(campaignId);
            if (user.isPresent() && campaign.isPresent() && campaignUtils.userIsInCampaign(user.get().getId(), campaign.get())) {
                List<CampaignGameModel> games = campaignGameRepository.findByCampaignId(campaignId);
                return HttpResponseModel.success("Campaign games", games);
            }

            return HttpResponseModel.error("Failed to get campaign games: invalid details provided", null);

        } catch (Exception e) {
            return HttpResponseModel.error("Failed to get campaign games", null);
        }
    }

    public HttpResponseModel<Integer> saveNewCampaignGame(String userEmail, UUID campaignId, CampaignGameModel game) {
        try {
            Optional<MemberDTO> gameReporter = personService.getPersonByEmail(userEmail);
            Optional<CampaignModel> campaign = campaignService.getCampaignById(campaignId);
            if (gameReporter.isPresent() && campaign.isPresent() && campaignUtils.userIsInCampaign(gameReporter.get().getId(), campaign.get())) {
                CampaignGameModel saved = campaignGameRepository.save(game);
                return HttpResponseModel.success("New game saved", saved.getId());
            }
            return HttpResponseModel.error("New game was not saved. Invalid details provided.", null);
        } catch (Exception e) {
            return HttpResponseModel.error("New game was not saved", null);
        }
    }
}
