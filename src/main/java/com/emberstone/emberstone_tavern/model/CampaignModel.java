package com.emberstone.emberstone_tavern.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="campaign")
public class CampaignModel {
    public enum CampaignStatus {
        ACTIVE, COMPLETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private PersonModel owner;

    @Column(name = "icon_link")
    private String iconLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    @ColumnTransformer(write="?::campaign_status_types")
    private CampaignStatus campaignStatus;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "campaign_person_join",
            joinColumns = @JoinColumn(name="campaign_id"),
            inverseJoinColumns = @JoinColumn(name="player_id")
    )
    private Set<PersonModel> members;
}
