package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="regiment_of_renown")
public class RegimentOfRenown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "grand_alliance_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GrandAllianceModel grandAlliance;
}
