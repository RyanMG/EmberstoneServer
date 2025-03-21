package com.emberstone.emberstone_tavern.model.campaign;

import com.emberstone.emberstone_tavern.model.PersonModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="campaign_game")
public class CampaignGameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "winner_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel winner;

    @Column(name = "winner_id")
    private UUID winnerId;

    @OneToOne()
    @JoinColumn(name = "opponent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PersonModel opponent;

    @Column(name = "opponent_id")
    private UUID opponentId;

    @Column(name = "winner_score")
    private Integer winnerScore;

    @Column(name = "opponent_score")
    private Integer opponentScore;

    @OneToOne()
    @JoinColumn(name = "campaign_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CampaignModel campaign;

    @Column(name = "campaign_id")
    private UUID campaignId;

    @Column(name = "game_date")
    private Date gameDate;

    @Column(name = "mission_played")
    private String missionPlayed;

    @Column(name = "twist")
    private String twist;

    @Column(name = "rounds")
    private Integer rounds;

    @Column(name = "story")
    private String story;
}
