package com.emberstone.emberstone_tavern.model.roster;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="regiment")
public class RegimentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roster_id")
    private UUID rosterId;

    @Transient
    private Set<UnitModel> units;
}
