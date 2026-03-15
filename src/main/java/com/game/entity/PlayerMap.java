package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 玩家地图位置
 */
@Data
@Entity
@Table(name = "t_player_map")
public class PlayerMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 当前区域ID
    private Integer zoneId;
    
    // 当前区域类型
    private String zoneType;
    
    // 坐标
    private Integer x;
    private Integer y;
    
    // 进入时间
    private Long enterTime;
}
