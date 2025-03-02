package com.emberstone.emberstone_tavern.model.roster;

import com.emberstone.emberstone_tavern.model.GrandAllianceModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="regiment_of_renown")
public class RegimentOfRenownModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne()
    @JoinColumn(name = "grand_alliance_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GrandAllianceModel grandAlliance;
}
