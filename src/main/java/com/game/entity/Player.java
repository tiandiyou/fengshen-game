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
