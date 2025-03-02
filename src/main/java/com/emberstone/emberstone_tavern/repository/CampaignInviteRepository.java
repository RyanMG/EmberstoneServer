package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.campaign.CampaignPersonInviteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CampaignInviteRepository extends JpaRepository<CampaignPersonInviteModel, Integer> {

    @Query("SELECT cpi FROM CampaignPersonInviteModel cpi WHERE cpi.playerId = :playerId")
    List<CampaignPersonInviteModel> getInvitesForPerson(@Param("playerId") UUID playerId);

}
