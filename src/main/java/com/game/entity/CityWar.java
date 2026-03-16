package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 城战记录
 */
@Data
@Entity
@Table(name = "t_city_war")
public class CityWar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long cityId;        // 城池ID
    private Long attackerId;    // 攻击方玩家ID
    private Long defenderId;    // 防守方玩家ID
    
    private String attackerTeam;  // 攻击方队伍(JSON)
    private String defenderTeam;   // 防守方队伍(JSON)
    
    private Integer attackerPower;  // 攻击方战力
    private Integer defenderPower;  // 防守方战力
    
    private String result;       // 胜/负
    private Long warTime;        // 战斗时间
    
    private Integer rewardGold;   // 奖励金币
    private Integer rewardExp;    // 奖励经验
}
