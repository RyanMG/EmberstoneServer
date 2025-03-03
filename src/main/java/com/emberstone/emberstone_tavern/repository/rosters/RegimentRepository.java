package com.emberstone.emberstone_tavern.repository.rosters;

import com.emberstone.emberstone_tavern.model.roster.RegimentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimentRepository extends JpaRepository<RegimentModel, Integer> {
}
