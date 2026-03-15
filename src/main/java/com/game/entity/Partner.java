package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 武将实体 v2.1
 * 包含等级成长、星级系统
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
    
    // 武将ID(对应PartnerData)
    private Integer partnerId;
    
    // 武将名称
    private String name;
    
    // 图标
    private String icon;
    
    // 品质: red/orange/purple/blue
    private String quality;
    
    // 兵种: 步/骑/弓/策
    private String type;
    
    // ========== 基础属性 (1级) ==========
    private Integer atk;        // 武力
    private Integer intelligence; // 智力
    private Integer lead;       // 统率
    private Integer speed;     // 速度
    
    // ========== 成长值 ==========
    private Integer growthAtk;  // 武力成长
    private Integer growthInt;  // 智力成长
    private Integer growthLead; // 统率成长
    private Integer growthSpeed; // 速度成长
    
    // ========== 等级系统 ==========
    private Integer level = 1;   // 等级
    private Integer exp = 0;    // 经验
    private Integer maxLevel;   // 等级上限
    
    // ========== 星级系统 ==========
    private Integer star = 1;   // 星级 (1-6)
    private Integer starExp = 0; // 升星经验
    
    // ========== 战斗属性 ==========
    private Integer maxTroops = 10000; // 最大兵量
    
    // ========== 技能 ==========
    private String skill1;  // 主动技能
    private String skill2; // 被动技能
    
    // 是否选中上阵
    private Boolean selected = false;
    
    // ========== 计算方法 ==========
    
    /**
     * 获取星级属性加成百分比
     */
    public double getStarBonus() {
        // 1星=0%, 2星=10%, 3星=25%, 4星=45%, 5星=70%, 6星=100%
        int[] bonus = {0, 10, 25, 45, 70, 100};
        return bonus[star] / 100.0;
    }
    
    /**
     * 获取当前武力
     */
    public int getCurrentAtk() {
        // 基础 + 等级成长 + 星级加成
        double base = atk + (level - 1) * growthAtk;
        return (int) (base * (1 + getStarBonus()));
    }
    
    /**
     * 获取当前智力
     */
    public int getCurrentInt() {
        double base = intelligence + (level - 1) * growthInt;
        return (int) (base * (1 + getStarBonus()));
    }
    
    /**
     * 获取当前统率
     */
    public int getCurrentLead() {
        double base = lead + (level - 1) * growthLead;
        return (int) (base * (1 + getStarBonus()));
    }
    
    /**
     * 获取当前速度
     */
    public int getCurrentSpeed() {
        double base = speed + (level - 1) * growthSpeed;
        return (int) (base * (1 + getStarBonus()));
    }
    
    /**
     * 获取当前兵量
     */
    public int getCurrentTroops() {
        return 10000 + level * 500;
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
        return exp >= getExpForNextLevel() && level < maxLevel;
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
    
    /**
     * 升星所需武将数
     */
    public int getStarCost() {
        // 2星需要2个, 3星需要2个...
        return star >= 6 ? 0 : 2;
    }
    
    /**
     * 升星
     */
    public void promoteStar() {
        if (star < 6) {
            star++;
        }
    }
    
    /**
     * 计算战力
     */
    public int getZhanli() {
        int base = (getCurrentAtk() + getCurrentInt() + getCurrentLead()) * 2 
                   + getCurrentSpeed();
        int skillBonus = (skill1 != null ? 50 : 0) + (skill2 != null ? 50 : 0);
        return base + getCurrentTroops() / 100 + skillBonus;
    }
    
    /**
     * 物理伤害计算 (方案B)
     */
    public int calcPhysicalDamage(int enemyLead) {
        double dmg = getCurrentAtk() * (1 - (double)enemyLead / (enemyLead + 200));
        return Math.max(1, (int) dmg);
    }
    
    /**
     * 法术伤害计算 (方案B)
     */
    public int calcMagicDamage(int enemyInt) {
        double dmg = getCurrentInt() * (1 - (double)enemyInt / (enemyInt + 200));
        return Math.max(1, (int) dmg);
    }
}
