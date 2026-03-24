package com.game.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "map_zone")
public class MapZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "zone_id")
    private Integer zoneId;
    
    private String name;
    private String type;  // birth, resource, capital, special, boss
    
    @Column(name = "level_req")
    private Integer levelReq;
    
    @Column(name = "gold_output")
    private Integer goldOutput;
    
    @Column(name = "lingqi_output")
    private Integer lingqiOutput;
    
    @Column(name = "relic_type")
    private String relicType;  // 山洞, 古墓, 仙府, 无
    
    private Integer x;
    private Integer y;
    
    // 新增字段
    private String climate;  // 气候: sunny, rainy, snowy
    private String monster;   // 怪物类型
    private Integer monsterLevel;
    private Integer defense;  // 城防值
    private Boolean unlock;   // 是否解锁
    
    @Column(name = "owner_id")
    private Long ownerId;    // 占领者
    
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getZoneId() { return zoneId; }
    public void setZoneId(Integer zoneId) { this.zoneId = zoneId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Integer getLevelReq() { return levelReq; }
    public void setLevelReq(Integer levelReq) { this.levelReq = levelReq; }
    
    public Integer getGoldOutput() { return goldOutput; }
    public void setGoldOutput(Integer goldOutput) { this.goldOutput = goldOutput; }
    
    public Integer getLingqiOutput() { return lingqiOutput; }
    public void setLingqiOutput(Integer lingqiOutput) { this.lingqiOutput = lingqiOutput; }
    
    public String getRelicType() { return relicType; }
    public void setRelicType(String relicType) { this.relicType = relicType; }
    
    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }
    
    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }
    
    public String getClimate() { return climate; }
    public void setClimate(String climate) { this.climate = climate; }
    
    public String getMonster() { return monster; }
    public void setMonster(String monster) { this.monster = monster; }
    
    public Integer getMonsterLevel() { return monsterLevel; }
    public void setMonsterLevel(Integer monsterLevel) { this.monsterLevel = monsterLevel; }
    
    public Integer getDefense() { return defense; }
    public void setDefense(Integer defense) { this.defense = defense; }
    
    public Boolean getUnlock() { return unlock; }
    public void setUnlock(Boolean unlock) { this.unlock = unlock; }
    
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
