package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 战法实体
 * 包含主动战法、被动战法、指挥战法
 */
@Data
@Entity
@Table(name = "t_skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 战法ID(对应GameData)
    private Integer skillId;
    
    // 战法名称
    private String name;
    
    // 图标
    private String icon;
    
    // 战法类型: 主动/被动/指挥/联动
    private String type;
    
    // 效果类型: 伤害/治疗/增益/减益/控制/护盾
    private String effectType;
    
    // 效果值(百分比)
    private Integer effectValue;
    
    // 冷却回合(主动战法)
    private Integer cooldown = 0;
    
    // 消耗(如mp/怒气)
    private Integer cost = 0;
    
    // 目标类型: 单体/群体/自身/友军
    private String targetType;
    
    // 描述
    private String description;
}
