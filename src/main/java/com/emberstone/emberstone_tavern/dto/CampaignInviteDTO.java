package com.emberstone.emberstone_tavern.dto;

import lombok.Data;

@Data
public class CampaignInviteDTO {
    private Integer id;

    private MemberDTO owner;

    private MemberDTO player;

    private CampaignOverviewDTO campaignOverview;
}
