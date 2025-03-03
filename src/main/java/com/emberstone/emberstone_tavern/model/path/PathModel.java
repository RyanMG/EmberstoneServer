package com.emberstone.emberstone_tavern.model.path;

import com.emberstone.emberstone_tavern.model.roster.UnitTypeModel;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="path")
public class PathModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "is_hero_path")
    private boolean isHeroPath;

    @OneToOne
    @JoinColumn(name = "unit_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UnitTypeModel pathUnitType;

    @Column(name = "unit_type_id")
    private Integer unitTypeId;
}
