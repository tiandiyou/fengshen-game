package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 坐骑实体
 */
@Data
@Entity
@Table(name = "t_mount")
public class Mount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 坐骑ID
    private Integer mountId;
    
    // 坐骑名称
    private String name;
    
    // 图标
    private String icon;
    
    // 品质: red/orange/purple/blue
    private String quality;
    
    // 基础属性
    private Integer atk;
    private Integer def;
    private Integer hp;
    private Integer speed;
    
    // 特技
    private String skill;
    
    // 是否已装备
    private Boolean equipped;
    
    // 星级
    private Integer star;
    
    // 获取战力
    public int getZhanli() {
        return (atk != null ? atk : 0) * 2 + 
               (def != null ? def : 0) * 2 + 
               (hp != null ? hp : 0) / 10 +
               (speed != null ? speed : 0);
    }
}
