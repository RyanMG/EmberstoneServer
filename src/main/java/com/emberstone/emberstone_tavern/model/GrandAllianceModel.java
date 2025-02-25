package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="grand_alliance")
public class GrandAllianceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}
