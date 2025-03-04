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

    @Column(name = "regiment_number")
    private Integer regimentNumber;

    @Column(name = "regiment_name")
    private String regimentName;

    @Column(name = "is_general")
    private Boolean isGeneral;

    @Column(name = "is_auxiliary")
    private Boolean isAuxiliary;

    @Transient
    private Set<UnitModel> units;
}
