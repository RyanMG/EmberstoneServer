package com.emberstone.emberstone_tavern.repository.units;

import com.emberstone.emberstone_tavern.model.roster.UnitModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UnitRepository extends JpaRepository<UnitModel, Integer> {
    @Query("SELECT COUNT(u) FROM UnitModel u WHERE u.regimentId = :regimentId")
    Integer getCountOfUnitsInRegiment(@Param("regimentId") Integer regimentId);
}
