package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.CampaignModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignModel, UUID> {

    @Query("SELECT c FROM CampaignModel c WHERE c.id IN (select cpj.campaign_id from CampaignPersonJoin cpj where cpj.player_id = :userId )")
    Set<CampaignModel> getAllCampaignsForUser(@Param("userId") UUID userId);
}
