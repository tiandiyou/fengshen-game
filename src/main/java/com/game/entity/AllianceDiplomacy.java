package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_alliance_diplomacy")
public class AllianceDiplomacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long allianceId1;
    private Long allianceId2;
    private String status; // friendly/hostile/neutral
    private Long updateTime;
}
