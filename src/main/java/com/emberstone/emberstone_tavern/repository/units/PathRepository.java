package com.emberstone.emberstone_tavern.repository.units;

import com.emberstone.emberstone_tavern.model.path.PathModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PathRepository extends JpaRepository<PathModel, Integer> {
    @Query("SELECT p FROM PathModel p WHERE p.isHeroPath = :isHero AND p.unitTypeId = :unitTypeId")
    List<PathModel> getPathsByUnitType(@Param("isHero") boolean isHero, @Param("unitTypeId") Integer unitTypeId);
}
