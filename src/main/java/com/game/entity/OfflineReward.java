package com.game.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offline_reward")
public class OfflineReward {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "player_id")
    private Long playerId;
    
    // 最后在线时间
    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;
    
    // 累积灵气
    @Column(name = "accumulated_lingqi")
    private Integer accumulatedLingqi;
    
    // 累积金币
    @Column(name = "accumulated_gold")
    private Integer accumulatedGold;
    
    // 已领取（防止重复领取）
    @Column(name = "claimed")
    private Boolean claimed;
    
    // VIP等级影响离线上限（小时）
    @Column(name = "max_offline_hours")
    private Integer maxOfflineHours;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public LocalDateTime getLastOnlineTime() { return lastOnlineTime; }
    public void setLastOnlineTime(LocalDateTime lastOnlineTime) { this.lastOnlineTime = lastOnlineTime; }
    
    public Integer getAccumulatedLingqi() { return accumulatedLingqi; }
    public void setAccumulatedLingqi(Integer accumulatedLingqi) { this.accumulatedLingqi = accumulatedLingqi; }
    
    public Integer getAccumulatedGold() { return accumulatedGold; }
    public void setAccumulatedGold(Integer accumulatedGold) { this.accumulatedGold = accumulatedGold; }
    
    public Boolean getClaimed() { return claimed; }
    public void setClaimed(Boolean claimed) { this.claimed = claimed; }
    
    public Integer getMaxOfflineHours() { return maxOfflineHours; }
    public void setMaxOfflineHours(Integer maxOfflineHours) { this.maxOfflineHours = maxOfflineHours; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}