package com.emberstone.emberstone_tavern.repository;

import com.emberstone.emberstone_tavern.model.CampaignSettingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignSettingRepository extends JpaRepository<CampaignSettingModel, Integer> {

}
