package com.emberstone.emberstone_tavern.repository.campaign;

import com.emberstone.emberstone_tavern.model.campaign.CampaignGameModel;
import com.emberstone.emberstone_tavern.model.campaign.CampaignPersonInviteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignGameRepository extends JpaRepository<CampaignGameModel, Integer> {

    @Query("SELECT cg FROM CampaignGameModel cg WHERE cg.campaignId = :campaignId")
    List<CampaignGameModel> findByCampaignId(@Param("campaignId") UUID campaignId);
}
