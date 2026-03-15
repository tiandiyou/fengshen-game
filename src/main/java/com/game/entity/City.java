package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 城池实体
 */
@Data
@Entity
@Table(name = "t_city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 城池名称
    private String name;
    
    // 城池等级
    private Integer level;
    
    // 资源产出
    private Integer goldOutput;
    private Integer lingqiOutput;
    
    // 民心/士气
    private Integer morale;
    
    // 建筑等级
    private Integer palaceLevel;    // 主殿
    private Integer barracksLevel; // 兵营
    private Integer warehouseLevel; // 仓库
    private Integer marketLevel;    // 市场
}
