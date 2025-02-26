package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.RosterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RosterRepository extends JpaRepository<RosterModel, Integer> {
    @Query("SELECT r FROM RosterModel r WHERE r.playerId = :playerId")
    Set<RosterModel> getAllByPlayerId(@Param("playerId") UUID playerId);

    @Query("SELECT r FROM RosterModel r WHERE r.playerId = :playerId AND r.campaignId = :campaignId")
    Optional<RosterModel> getRosterByPlayerIdAndCampaignId(@Param("playerId") UUID playerId,  @Param("campaignId") UUID campaignId);
}
