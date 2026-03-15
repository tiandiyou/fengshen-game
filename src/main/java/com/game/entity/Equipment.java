package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 装备实体
 */
@Data
@Entity
@Table(name = "t_equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 装备ID
    private Integer equipmentId;
    
    // 装备名称
    private String name;
    
    // 图标
    private String icon;
    
    // 装备类型: weapon(武器)/armor(防具)/accessory(饰品)
    private String type;
    
    // 品质: red/orange/purple/blue/green/gray
    private String quality;
    
    // 装备属性
    private Integer atk;
    private Integer def;
    private Integer hp;
    private Integer speed;
    
    // 是否穿戴
    private Boolean equipped;
    
    // 所属武将ID(如果穿戴)
    private Long partnerId;
    
    // 星级
    private Integer star;
    
    // 获取战力
    public int getZhanli() {
        return (atk != null ? atk : 0) * 2 + 
               (def != null ? def : 0) * 2 + 
               (hp != null ? hp : 0) / 10;
    }
}
