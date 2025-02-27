package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.FactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FactionRepository extends JpaRepository<FactionModel, Integer> {

    @Query("SELECT f FROM FactionModel f WHERE f.grandAllianceId = :grandAllianceId")
    List<FactionModel> getFactionsByGrandAlliance(@Param("grandAllianceId") Integer grandAllianceId);
}
