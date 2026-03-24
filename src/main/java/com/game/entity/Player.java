package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "t_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private Integer lingqi = 200;
    
    private Integer gold = 100;
    
    private Integer zhanli = 0;
    
    private Integer chapterId = 0;
    
    private Integer battleCount = 0;
    
    private Integer signinDays = 0;
    
    private String signinLastDate = "";
    
    // 新增: 玩家等级
    private Integer level = 1;
    
    // 新增: 经验值
    private Integer exp = 0;
    
    // 新增: 抽卡计数(用于保底)
    private Integer gachaCount = 0;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getLingqi() { return lingqi; }
    public void setLingqi(Integer lingqi) { this.lingqi = lingqi; }
    
    public Integer getGold() { return gold; }
    public void setGold(Integer gold) { this.gold = gold; }
    
    public Integer getZhanli() { return zhanli; }
    public void setZhanli(Integer zhanli) { this.zhanli = zhanli; }
    
    public Integer getChapterId() { return chapterId; }
    public void setChapterId(Integer chapterId) { this.chapterId = chapterId; }
    
    public Integer getBattleCount() { return battleCount; }
    public void setBattleCount(Integer battleCount) { this.battleCount = battleCount; }
    
    public Integer getSigninDays() { return signinDays; }
    public void setSigninDays(Integer signinDays) { this.signinDays = signinDays; }
    
    public String getSigninLastDate() { return signinLastDate; }
    public void setSigninLastDate(String signinLastDate) { this.signinLastDate = signinLastDate; }
    
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    
    public Integer getExp() { return exp; }
    public void setExp(Integer exp) { this.exp = exp; }
    
    public Integer getGachaCount() { return gachaCount; }
    public void setGachaCount(Integer gachaCount) { this.gachaCount = gachaCount; }
    
    // ========== Business Methods ==========
    
    // 等级所需经验公式: level * 100
    public int getExpForNextLevel() {
        return level * 100;
    }
    
    // 计算当前兵量: 10000 + level * 1000
    public int getTroops() {
        return 10000 + level * 1000;
    }
    
    // 升级所需经验
    public boolean canLevelUp() {
        return exp >= getExpForNextLevel();
    }
    
    // 升级
    public void levelUp() {
        if (canLevelUp()) {
            exp -= getExpForNextLevel();
            level++;
        }
    }
    
    // 增加经验
    public void addExp(int amount) {
        this.exp += amount;
        while (canLevelUp()) {
            levelUp();
        }
    }
}
