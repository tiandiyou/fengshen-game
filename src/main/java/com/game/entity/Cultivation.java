package com.game.entity;

import jakarta.persistence.*;

/**
 * 修仙实体
 * 记录玩家的修仙等级、经验、流派
 */
@Entity
@Table(name = "t_cultivation")
public class Cultivation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 流派: body(体修)/magic(法修)
    private String type;
    
    // 当前等级
    private Integer level;
    
    // 当前经验
    private Integer exp;
    
    // 累计修炼时间(秒)
    private Long totalTime;
    
    // 最后更新时间
    private Long lastUpdate;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getExp() { return exp; }
    public void setExp(Integer exp) { this.exp = exp; }
    
    public Long getTotalTime() { return totalTime; }
    public void setTotalTime(Long totalTime) { this.totalTime = totalTime; }
    
    public Long getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Long lastUpdate) { this.lastUpdate = lastUpdate; }
    
    // ========== Business Methods ==========
    
    // 获取升级所需经验
    public int getExpForNextLevel() {
        return level * 100;
    }
    
    // 获取属性加成
    public int getBonus() {
        return level * 5; // 每级+5%属性
    }
}
