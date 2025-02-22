package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="campaign_person_join")
public class CampaignPersonJoinModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_id")
    private UUID player_id;

    @Column(name = "campaign_id")
    private UUID campaign_id;
}
