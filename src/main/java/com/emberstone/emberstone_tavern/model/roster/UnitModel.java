package com.emberstone.emberstone_tavern.model.roster;

import com.emberstone.emberstone_tavern.model.emberstone_weapon.EmberstoneWeaponModel;
import com.emberstone.emberstone_tavern.model.path.PathModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="unit")
public class UnitModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "unit_number")
    private Integer unitNumber;

    @Column(name = "regiment_id")
    private Integer regimentId;

    @Column(name = "is_general")
    private Boolean isGeneral;

    @Column(name = "is_hero")
    private Boolean isHero;

    @Transient
    private EmberstoneWeaponModel emberstoneWeapon;

    @Column(name = "emberstone_weapon_id")
    private UUID emberstoneWeaponId;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "warscroll_name")
    private String warscrollName;

    @OneToOne
    @JoinColumn(name = "unit_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UnitTypeModel unitType;

    @Column(name = "unit_type_id")
    private Integer unitTypeId;

    @OneToOne
    @JoinColumn(name = "path_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PathModel path;

    @Column(name = "path_id")
    private Integer pathId;

    @Column(name = "path_rank")
    private Integer pathRank;

    @Column(name = "unit_cost")
    private Integer unitCost;

    @Column(name = "battle_wounds")
    private Integer battleWounds;

    @Column(name = "is_reinforced")
    private Boolean isReinforced;
}
