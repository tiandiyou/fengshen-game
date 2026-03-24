package com.game.entity;

import jakarta.persistence.*;

/**
 * 城池实体
 */
@Entity
@Table(name = "t_city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 城池名称
    private String name;
    
    // 城池等级
    private Integer level;
    
    // 资源产出
    private Integer goldOutput;
    private Integer lingqiOutput;
    
    // 民心/士气
    private Integer morale;
    
    // 建筑等级
    private Integer palaceLevel;    // 主殿
    private Integer barracksLevel; // 兵营
    private Integer warehouseLevel; // 仓库
    private Integer marketLevel;    // 市场
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getGoldOutput() { return goldOutput; }
    public void setGoldOutput(Integer goldOutput) { this.goldOutput = goldOutput; }
    
    public Integer getLingqiOutput() { return lingqiOutput; }
    public void setLingqiOutput(Integer lingqiOutput) { this.lingqiOutput = lingqiOutput; }
    
    public Integer getMorale() { return morale; }
    public void setMorale(Integer morale) { this.morale = morale; }
    
    public Integer getPalaceLevel() { return palaceLevel; }
    public void setPalaceLevel(Integer palaceLevel) { this.palaceLevel = palaceLevel; }
    
    public Integer getBarracksLevel() { return barracksLevel; }
    public void setBarracksLevel(Integer barracksLevel) { this.barracksLevel = barracksLevel; }
    
    public Integer getWarehouseLevel() { return warehouseLevel; }
    public void setWarehouseLevel(Integer warehouseLevel) { this.warehouseLevel = warehouseLevel; }
    
    public Integer getMarketLevel() { return marketLevel; }
    public void setMarketLevel(Integer marketLevel) { this.marketLevel = marketLevel; }
}
