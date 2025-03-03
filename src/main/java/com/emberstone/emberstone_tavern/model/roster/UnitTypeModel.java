package com.emberstone.emberstone_tavern.model.roster;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="unit_type")
public class UnitTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;
}
