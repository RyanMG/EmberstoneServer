package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="roster")
public class RosterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel player;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grand_alliance_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GrandAllianceModel grandAlliance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "faction_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FactionModel faction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ror_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RegimentOfRenown regimentOfRenown;
}
