package com.emberstone.emberstone_tavern.dto;

import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import lombok.Data;
import java.util.UUID;

@Data
public class CampaignOverviewDTO {

    private UUID id;

    private String title;

    private String description;

    private String iconLink;

    private UUID ownerId;

    private CampaignModel.CampaignStatus campaignStatus;
}
