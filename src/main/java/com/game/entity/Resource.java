package com.game.entity;

import jakarta.persistence.*;

/**
 * 资源实体
 */
@Entity
@Table(name = "t_resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private Long cityId;
    private Integer food;      // 粮食
    private Integer wood;      // 木材
    private Integer iron;     // 铁矿
    private Integer gold;     // 金币
    private Long updateTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public Integer getFood() { return food; }
    public void setFood(Integer food) { this.food = food; }
    
    public Integer getWood() { return wood; }
    public void setWood(Integer wood) { this.wood = wood; }
    
    public Integer getIron() { return iron; }
    public void setIron(Integer iron) { this.iron = iron; }
    
    public Integer getGold() { return gold; }
    public void setGold(Integer gold) { this.gold = gold; }
    
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
