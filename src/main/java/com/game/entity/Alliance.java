package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 联盟实体
 */
@Data
@Entity
@Table(name = "t_alliance")
public class Alliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 联盟名称
    private String name;
    
    // 盟主ID
    private Long leaderId;
    
    // 联盟公告
    private String notice;
    
    // 成员数量
    private Integer memberCount;
    
    // 最大成员
    private Integer maxMembers;
    
    // 联盟等级
    private Integer level;
    
    // 联盟经验
    private Integer exp;
    
    // 创建时间
    private Long createTime;
}
