package com.emberstone.emberstone_tavern.repository.rosters;

import com.emberstone.emberstone_tavern.model.roster.RosterGeneralModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RosterGeneralRepository extends JpaRepository<RosterGeneralModel, Integer> {
    @Query("SELECT rg FROM RosterGeneralModel rg WHERE rg.rosterId = :rosterId")
    Optional<RosterGeneralModel> getByRosterId(@Param("rosterId") UUID rosterId);

}
