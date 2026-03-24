package com.game.entity;

import jakarta.persistence.*;

/**
 * 资源田实体 - 玩家占领的资源田地
 */
@Entity
@Table(name = "t_resource_field")
public class ResourceField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;      // 玩家ID
    private Long mapZoneId;    // 地图区域ID
    private String fieldType;   // 资源田类型: farm(农田), lumber(伐木场), iron(铁矿), gold(金矿)
    private Integer level;      // 等级
    private Long createTime;    // 创建时间
    private Long updateTime;   // 更新时间
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Long getMapZoneId() { return mapZoneId; }
    public void setMapZoneId(Long mapZoneId) { this.mapZoneId = mapZoneId; }
    
    public String getFieldType() { return fieldType; }
    public void setFieldType(String fieldType) { this.fieldType = fieldType; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}