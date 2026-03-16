package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_war_credit")
public class WarCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private Long allianceId;
    private Long seasonId;
    private Integer credit;    // 战功
    private Integer kills;     // 击杀数
    private Integer battles;   // 战斗次数
    private Long updateTime;
}
