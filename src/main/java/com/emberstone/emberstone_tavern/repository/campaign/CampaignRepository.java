package com.emberstone.emberstone_tavern.repository.campaign;

import com.emberstone.emberstone_tavern.model.campaign.CampaignModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignModel, UUID> {

    @Query("SELECT c FROM CampaignModel c WHERE c.id IN (select cpj.campaign_id from CampaignPersonJoinModel cpj where cpj.player_id = :userId) OR (c.ownerId = :userId) AND c.campaignStatus = 'ACTIVE'")
    Set<CampaignModel> getAllActiveCampaignsForUser(@Param("userId") UUID userId);

    @Query("SELECT c FROM CampaignModel c WHERE (c.id IN (select cpj.campaign_id from CampaignPersonJoinModel cpj where cpj.player_id = :userId) OR (c.ownerId = :userId)) AND c.campaignStatus = 'COMPLETE'")
    Set<CampaignModel> getAllCompletedCampaignsForUser(@Param("userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CampaignModel c WHERE c.campaignCode = :code")
    boolean existsByCampaignCode(@Param("code") String code);

    CampaignModel getByCampaignCode(String campaignCode);
}
