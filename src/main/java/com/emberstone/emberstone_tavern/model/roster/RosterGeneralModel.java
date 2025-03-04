package com.emberstone.emberstone_tavern.model.roster;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="roster_general")
public class RosterGeneralModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roster_id")
    private UUID rosterId;

    @Column(name = "general_id")
    private Integer generalId;
}
