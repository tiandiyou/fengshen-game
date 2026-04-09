package com.game.entity;

import jakarta.persistence.*;

/**
 * 玩家地图位置
 */
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
    
    // ========== 移动相关字段 ==========
    // 是否正在移动中
    private Boolean isMoving;
    
    // 移动开始时间
    private Long moveStartTime;
    
    // 移动持续时间（毫秒）
    private Long moveDuration;
    
    // 目标区域ID（移动中）
    private Integer targetZoneId;
    
    // 移动冷却截止时间
    private Long moveCooldownUntil;
    
    // 起点区域ID（用于计算距离）
    private Integer fromZoneId;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Integer getZoneId() { return zoneId; }
    public void setZoneId(Integer zoneId) { this.zoneId = zoneId; }
    
    public String getZoneType() { return zoneType; }
    public void setZoneType(String zoneType) { this.zoneType = zoneType; }
    
    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }
    
    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }
    
    public Long getEnterTime() { return enterTime; }
    public void setEnterTime(Long enterTime) { this.enterTime = enterTime; }
    
    // 移动相关 getter/setter
    public Boolean getIsMoving() { return isMoving; }
    public void setIsMoving(Boolean isMoving) { this.isMoving = isMoving; }
    
    public Long getMoveStartTime() { return moveStartTime; }
    public void setMoveStartTime(Long moveStartTime) { this.moveStartTime = moveStartTime; }
    
    public Long getMoveDuration() { return moveDuration; }
    public void setMoveDuration(Long moveDuration) { this.moveDuration = moveDuration; }
    
    public Integer getTargetZoneId() { return targetZoneId; }
    public void setTargetZoneId(Integer targetZoneId) { this.targetZoneId = targetZoneId; }
    
    public Long getMoveCooldownUntil() { return moveCooldownUntil; }
    public void setMoveCooldownUntil(Long moveCooldownUntil) { this.moveCooldownUntil = moveCooldownUntil; }
    
    public Integer getFromZoneId() { return fromZoneId; }
    public void setFromZoneId(Integer fromZoneId) { this.fromZoneId = fromZoneId; }
}
