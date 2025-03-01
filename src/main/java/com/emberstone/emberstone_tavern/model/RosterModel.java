package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="roster")
public class RosterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "player_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel player;

    @Column(name = "player_id")
    private UUID playerId;

    @Column(name = "campaign_id")
    private UUID campaignId;

    private String name;

    @Column(name = "point_total")
    private Integer pointTotal;

    @Column(name = "has_faction_terrain")
    private boolean hasFactionTerrain;

    @Column(name = "emberstone_total")
    private Integer emberstoneTotal;

    @Column(name = "emberstone_vault")
    private Integer emberstoneVault;

    @OneToOne()
    @JoinColumn(name = "grand_alliance_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GrandAllianceModel grandAlliance;

    @Column(name = "grand_alliance_id")
    private Integer grandAllianceId;

    @OneToOne()
    @JoinColumn(name = "faction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FactionModel faction;

    @Column(name = "faction_id")
    private Integer factionId;

    @OneToOne()
    @JoinColumn(name = "ror_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RegimentOfRenown regimentOfRenown;
}
