package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.campaign.CampaignPersonJoinModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CampaignPersonJoinRepository extends JpaRepository<CampaignPersonJoinModel, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CampaignPersonJoinModel c WHERE c.player_id = :userId AND c.campaign_id = :campaignId")
    Integer deletePlayerFromCampaign(@Param("userId") UUID userId, @Param("campaignId") UUID campaignId);
}
