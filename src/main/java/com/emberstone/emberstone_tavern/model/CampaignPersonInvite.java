package com.emberstone.emberstone_tavern.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="campaign_person_invite")
public class CampaignPersonInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel owner;

    @Column(name = "owner_id")
    @JsonIgnore
    private UUID ownerId;

    @OneToOne()
    @JoinColumn(name = "player_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel player;

    @Column(name = "player_id")
    @JsonIgnore
    private UUID playerId;

    @OneToOne()
    @JoinColumn(name = "campaign_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CampaignOverviewModel campaignOverview;

    @Column(name = "campaign_id")
    @JsonIgnore
    private UUID campaignId;

    @Column(name = "invite_date")
    private Date inviteDate;
}
