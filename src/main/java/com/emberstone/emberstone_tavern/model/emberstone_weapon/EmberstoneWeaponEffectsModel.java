package com.emberstone.emberstone_tavern.model.emberstone_weapon;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="emberstone_weapon_effects")
public class EmberstoneWeaponEffectsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UUID weaponId;

    private Integer effectId;
}
