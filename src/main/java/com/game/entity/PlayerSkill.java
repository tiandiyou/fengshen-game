package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_player_skill")
public class PlayerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private int skillId;
    private int stars;
    private long obtainTime;
    private long upgradeTime;
}
