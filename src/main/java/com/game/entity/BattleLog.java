package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_battle_log")
public class BattleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private Long cityId;
    private String battleType; // siege/pvp/arena
    private Long enemyId;
    private String result; // win/lose/draw
    private Integer attackPower;
    private Integer defendPower;
    private Integer attackLoss;
    private Integer defendLoss;
    private String log;
    private Long battleTime;
}
