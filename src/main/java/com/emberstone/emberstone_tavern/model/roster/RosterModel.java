package com.emberstone.emberstone_tavern.model.roster;

import com.emberstone.emberstone_tavern.model.FactionModel;
import com.emberstone.emberstone_tavern.model.GrandAllianceModel;
import com.emberstone.emberstone_tavern.model.PersonModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="roster")
public class RosterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Transient
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

    @OneToOne()
    @JoinTable(name = "roster_general",
            joinColumns =
                    { @JoinColumn(name = "roster_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "general_id", referencedColumnName = "id") })
    private UnitModel general;

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
    private RegimentOfRenownModel regimentOfRenown;

    @Column(name = "ror_id")
    private Integer rorId;

    @OneToMany(mappedBy = "rosterId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<RegimentModel> regiments;

    public boolean hasAnyEmptyRegiments() {
        return regiments == null || regiments.isEmpty() || regiments.stream().anyMatch(regiment -> regiment.getUnits().isEmpty());
    }
}
