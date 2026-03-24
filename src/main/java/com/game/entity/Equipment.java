package com.game.entity;

import jakarta.persistence.*;

/**
 * 装备实体
 */
@Entity
@Table(name = "t_equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private String type; // weapon/armor/helmet/horse
    private String name;
    private Integer quality; // 1-5
    private String special;  // 特技
    private Integer level;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getQuality() { return quality; }
    public void setQuality(Integer quality) { this.quality = quality; }
    
    public String getSpecial() { return special; }
    public void setSpecial(String special) { this.special = special; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
}
