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
}
