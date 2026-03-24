package com.game.entity;

import jakarta.persistence.*;

/**
 * 玩家技能实体
 */
@Entity
@Table(name = "t_player_skill")
public class PlayerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private int skillId;
    private int stars;
    private long obtainTime;
    private long upgradeTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public int getSkillId() { return skillId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }
    
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    
    public long getObtainTime() { return obtainTime; }
    public void setObtainTime(long obtainTime) { this.obtainTime = obtainTime; }
    
    public long getUpgradeTime() { return upgradeTime; }
    public void setUpgradeTime(long upgradeTime) { this.upgradeTime = upgradeTime; }
}
