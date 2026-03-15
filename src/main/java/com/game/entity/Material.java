package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 材料实体
 */
@Data
@Entity
@Table(name = "t_material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 材料类型: iron(玄铁)/steel(精钢)/meteoric(陨金)/crystal(天晶)
    private String type;
    
    // 材料名称
    private String name;
    
    // 图标
    private String icon;
    
    // 数量
    private Integer count;
    
    // 品质星级
    private Integer star;
}
