package com.emberstone.emberstone_tavern.repository.rosters;

import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegimentRepository extends JpaRepository<RegimentModel, Integer> {
    @Query("SELECT COUNT(r) FROM RegimentModel r WHERE r.rosterId = :rosterId")
    Integer getCountForRoster(@Param("rosterId") UUID rosterId);
}
