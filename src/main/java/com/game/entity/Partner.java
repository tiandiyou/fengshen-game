package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 武将实体
 * 包含六维属性、战法、成长系统
 */
@Data
@Entity
@Table(name = "t_partner")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 武将ID(对应GameData)
    private Integer partnerId;
    
    // 武将名称
    private String name;
    
    // 图标
    private String icon;
    
    // 品质: red(隐藏红将)/orange/purple/blue
    private String quality;
    
    // 兵种: 步/骑/弓/策
    private String type;
    
    // ========== 六维属性 ==========
    // 武力 - 物理攻击
    private Integer atk = 0;
    
    // 智力 - 法术攻击/法术防御
    private Integer intelligence = 70;
    
    // 统率 - 物理防御
    private Integer lead = 70;
    
    // 速度 - 行动顺序
    private Integer speed = 50;
    
    // 政令 - 内政产出
    private Integer politics = 30;
    
    // 军事 - 战斗相关
    private Integer military = 60;
    
    // 魅力 - 抽卡概率
    private Integer charm = 50;
    
    // ========== 战斗属性 ==========
    // 兵量(由玩家等级决定，这里记录最大兵量)
    private Integer maxTroops = 10000;
    
    // 等级
    private Integer level = 1;
    
    // 星级(0-5)
    private Integer star = 0;
    
    // 经验
    private Integer exp = 0;
    
    // ========== 战法 ==========
    // 战法列表(JSON格式)
    private String skills;
    
    // 主动战法
    private String activeSkill;
    
    // 被动战法
    private String passiveSkill;
    
    // 是否选中上阵
    private Boolean selected = false;
    
    // ========== 成长值 ==========
    // 武力成长
    private Integer growthAtk = 5;
    
    // 智力成长
    private Integer growthInt = 3;
    
    // 统率成长
    private Integer growthLead = 3;
    
    // ========== 计算方法 ==========
    
    /**
     * 计算当前攻击力
     * 基础 + (等级-1) * 成长值
     */
    public int getCurrentAtk() {
        return atk + (level - 1) * growthAtk;
    }
    
    /**
     * 计算当前智力
     */
    public int getCurrentInt() {
        return intelligence + (level - 1) * growthInt;
    }
    
    /**
     * 计算当前统率(物理防御)
     */
    public int getCurrentLead() {
        return lead + (level - 1) * growthLead;
    }
    
    /**
     * 物理伤害 = 武力 - 敌方统率
     */
    public int calcPhysicalDamage(int enemyLead) {
        return Math.max(1, getCurrentAtk() - enemyLead);
    }
    
    /**
     * 法术伤害 = 智力 - 敌方智力
     */
    public int calcMagicDamage(int enemyInt) {
        return Math.max(1, getCurrentInt() - enemyInt);
    }
    
    /**
     * 获取战力值
     */
    public int getZhanli() {
        return getCurrentAtk() * 2 + getCurrentInt() * 2 + getCurrentLead() * 2 + maxTroops / 100;
    }
    
    /**
     * 升级所需经验
     */
    public int getExpForNextLevel() {
        return level * 100;
    }
    
    /**
     * 是否可以升级
     */
    public boolean canLevelUp() {
        return exp >= getExpForNextLevel();
    }
    
    /**
     * 升级
     */
    public void levelUp() {
        if (canLevelUp()) {
            exp -= getExpForNextLevel();
            level++;
        }
    }
    
    /**
     * 增加经验
     */
    public void addExp(int amount) {
        this.exp += amount;
        while (canLevelUp()) {
            levelUp();
        }
    }
}
