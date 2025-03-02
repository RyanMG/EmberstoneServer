package com.emberstone.emberstone_tavern.util;

import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public final class CampaignUtils {

    private static final String codeparts = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateCampaignInviteCode() {
        char[] code = new char[6];
        Random rnd = new Random();
        for (int i = 0; i < code.length; i++) {
            int index = (int) (rnd.nextFloat() * codeparts.length());
            code[i] = codeparts.charAt(index);
        }
        return new String(code);
    }

    public boolean userIsCampaignOwner(UUID userId, UUID campaignOwnerId) {
        return userId.equals(campaignOwnerId);
    }

    public boolean userIsCampaignMember(UUID userId, CampaignModel campaign) {
        Set<UUID> memberIds = campaign.getMembers().stream().map(PersonModel::getId).collect(Collectors.toSet());
        return memberIds.contains(userId);
    }

    public boolean userIsInCampaign(UUID userId, CampaignModel campaign) {
        return userIsCampaignOwner(userId, campaign.getOwnerId()) || userIsCampaignMember(userId, campaign);
    }
}
