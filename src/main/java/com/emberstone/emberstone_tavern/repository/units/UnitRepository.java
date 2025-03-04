package com.emberstone.emberstone_tavern.repository.units;

import com.emberstone.emberstone_tavern.model.roster.UnitModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<UnitModel, Integer> {
}
