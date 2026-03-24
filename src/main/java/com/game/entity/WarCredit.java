package com.game.entity;

import jakarta.persistence.*;

/**
 * 战功实体
 */
@Entity
@Table(name = "t_war_credit")
public class WarCredit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private Long allianceId;
    private Long seasonId;
    private Integer credit;    // 战功
    private Integer kills;     // 击杀数
    private Integer battles;   // 战斗次数
    private Long updateTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Long getAllianceId() { return allianceId; }
    public void setAllianceId(Long allianceId) { this.allianceId = allianceId; }
    
    public Long getSeasonId() { return seasonId; }
    public void setSeasonId(Long seasonId) { this.seasonId = seasonId; }
    
    public Integer getCredit() { return credit; }
    public void setCredit(Integer credit) { this.credit = credit; }
    
    public Integer getKills() { return kills; }
    public void setKills(Integer kills) { this.kills = kills; }
    
    public Integer getBattles() { return battles; }
    public void setBattles(Integer battles) { this.battles = battles; }
    
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
