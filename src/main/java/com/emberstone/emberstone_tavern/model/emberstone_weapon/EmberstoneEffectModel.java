package com.emberstone.emberstone_tavern.model.emberstone_weapon;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="emberstone_effect")
public class EmberstoneEffectModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;
}
