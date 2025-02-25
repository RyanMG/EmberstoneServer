package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="faction")
public class FactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grand_alliance_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GrandAllianceModel grandAlliance;
}
