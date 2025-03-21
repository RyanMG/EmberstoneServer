package com.emberstone.emberstone_tavern.model.campaign;

import com.emberstone.emberstone_tavern.model.PersonModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Date;
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

    @Column(name= "campaign_code")
    private String campaignCode;

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel owner;

    @Column(name = "owner_id")
    @JsonIgnore
    private UUID ownerId;

    @OneToOne()
    @JoinColumn(name = "campaign_setting_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CampaignSettingModel campaignSetting;

    @Column(name = "campaign_setting_id")
    private Integer campaignSettingId;

    @Column(name = "icon_link")
    private String iconLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CampaignStatus campaignStatus;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "campaign_person_join",
            joinColumns = @JoinColumn(name="campaign_id"),
            inverseJoinColumns = @JoinColumn(name="player_id")
    )
    private Set<PersonModel> members;
}
