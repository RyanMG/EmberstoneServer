package com.emberstone.emberstone_tavern.model.campaign;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="campaign_setting")
public class CampaignSettingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
