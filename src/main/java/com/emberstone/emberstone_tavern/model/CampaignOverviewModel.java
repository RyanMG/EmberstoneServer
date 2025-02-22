package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import java.util.UUID;

@Data
@Entity
@Table(name="campaign")
public class CampaignOverviewModel {
    public enum CampaignStatus {
        ACTIVE, COMPLETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Column(name = "icon_link")
    private String iconLink;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CampaignStatus campaignStatus;
}
