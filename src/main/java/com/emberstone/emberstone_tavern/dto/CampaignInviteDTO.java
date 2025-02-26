package com.emberstone.emberstone_tavern.dto;

import com.emberstone.emberstone_tavern.model.CampaignOverviewModel;
import lombok.Data;

@Data
public class CampaignInviteDTO {
    private Integer id;

    private MemberDTO owner;

    private MemberDTO player;

    private CampaignOverviewModel campaignOverview;
}
