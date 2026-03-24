package com.game.entity;

import jakarta.persistence.*;

/**
 * 宝物实体
 */
@Entity
@Table(name = "t_treasure")
public class Treasure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private Integer treasureId;
    private String name;
    private String icon;
    private String bonus;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Integer getTreasureId() { return treasureId; }
    public void setTreasureId(Integer treasureId) { this.treasureId = treasureId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getBonus() { return bonus; }
    public void setBonus(String bonus) { this.bonus = bonus; }
}
