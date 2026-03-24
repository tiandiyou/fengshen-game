package com.game.entity;

import jakarta.persistence.*;

/**
 * 坐骑实体
 */
@Entity
@Table(name = "t_mount")
public class Mount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 坐骑ID
    private Integer mountId;
    
    // 坐骑名称
    private String name;
    
    // 图标
    private String icon;
    
    // 品质: red/orange/purple/blue
    private String quality;
    
    // 基础属性
    private Integer atk;
    private Integer def;
    private Integer hp;
    private Integer speed;
    
    // 特技
    private String skill;
    
    // 是否已装备
    private Boolean equipped;
    
    // 星级
    private Integer star;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Integer getMountId() { return mountId; }
    public void setMountId(Integer mountId) { this.mountId = mountId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    
    public Integer getAtk() { return atk; }
    public void setAtk(Integer atk) { this.atk = atk; }
    
    public Integer getDef() { return def; }
    public void setDef(Integer def) { this.def = def; }
    
    public Integer getHp() { return hp; }
    public void setHp(Integer hp) { this.hp = hp; }
    
    public Integer getSpeed() { return speed; }
    public void setSpeed(Integer speed) { this.speed = speed; }
    
    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }
    
    public Boolean getEquipped() { return equipped; }
    public void setEquipped(Boolean equipped) { this.equipped = equipped; }
    
    public Integer getStar() { return star; }
    public void setStar(Integer star) { this.star = star; }
    
    // ========== Business Methods ==========
    
    // 获取战力
    public int getZhanli() {
        return (atk != null ? atk : 0) * 2 + 
               (def != null ? def : 0) * 2 + 
               (hp != null ? hp : 0) / 10 +
               (speed != null ? speed : 0);
    }
}
