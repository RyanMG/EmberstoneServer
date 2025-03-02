package com.emberstone.emberstone_tavern.model.emberstone_weapon;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="emberstone_weapon")
public class EmberstoneWeaponModel {
    public enum EmberstoneWeaponType {
        RANGED, MELEE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "emberstone_weapon_type")
    private EmberstoneWeaponType emberstoneWeaponType;

    @ManyToMany
    @JoinTable(
            name = "emberstone_weapon_effects",
            joinColumns = @JoinColumn(name = "effectId"),
            inverseJoinColumns = @JoinColumn(name = "weaponId")
    )
    private Set<EmberstoneEffectModel> effects;
}
