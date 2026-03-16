package com.game.config;

/**
 * SLG战斗平衡配置
 * 参考三国志战略版
 */
public class BattleBalanceConfig {
    
    // ==================== 兵种相克 ====================
    // 攻击克制: 克+20%伤害, 被克-20%伤害
    public static final String[][] UNIT_COUNTER = {
        {"骑", "步"},  // 骑克步
        {"步", "弓"},  // 步克弓
        {"弓", "骑"},  // 弓克骑
        {"策", "无"}   // 策被所有克
    };
    
    // 兵种基础属性
    public static final java.util.Map<String, java.util.Map<String, Integer>> UNIT_STATS = 
        java.util.Map.of(
            "步", java.util.Map.of("atk", 100, "def", 100, "hp", 1000, "speed", 30),
            "骑", java.util.Map.of("atk", 120, "def", 80, "hp", 800, "speed", 50),
            "弓", java.util.Map.of("atk", 110, "def", 60, "hp", 700, "speed", 40),
            "策", java.util.Map.of("atk", 130, "def", 50, "hp", 600, "speed", 35)
        );
    
    // ==================== 战法类型 ====================
    public enum SkillType {
        ACTIVE,   // 主动战法
        PASSIVE,  // 被动战法  
        COMMAND   // 指挥战法
    }
    
    // 战法效果类型
    public enum EffectType {
        DAMAGE,      // 伤害
        HEAL,        // 治疗
        BUFF,        // 增益
        DEBUFF,      // 削弱
        CONTROL,     // 控制
        COUNTER      // 反击
    }
    
    // ==================== 品质平衡 ====================
    // 品质差异小，主要靠战法和搭配
    public static final java.util.Map<String, java.util.Map<String, Double>> QUALITY_MULT = 
        java.util.Map.of(
            "red", java.util.Map.of("atk", 1.3, "hp", 1.3, "skillRate", 1.0),
            "orange", java.util.Map.of("atk", 1.15, "hp", 1.15, "skillRate", 0.9),
            "purple", java.util.Map.of("atk", 1.0, "hp", 1.0, "skillRate", 0.8),
            "blue", java.util.Map.of("atk", 0.85, "hp", 0.85, "skillRate", 0.7)
        );
    
    // ==================== 伤害计算 ====================
    public static final int BASE_DAMAGE = 100;      // 基础伤害
    public static final double CRIT_MULT = 1.5;    // 暴击伤害
    public static final double COUNTER_MULT = 1.2;  // 反击伤害
    
    // ==================== 属性上限 ====================
    public static final int MAX_ATK = 500;    // 最大攻击
    public static final int MAX_HP = 10000;  // 最大兵力
    public static final int MAX_SPEED = 200; // 最大速度
    
    // ==================== 计算公式 ====================
    
    /**
     * 计算兵种克制系数
     */
    public static double getCounterBonus(String attackUnit, String defendUnit) {
        if (attackUnit == null || defendUnit == null) return 1.0;
        
        for (String[] pair : UNIT_COUNTER) {
            if (pair[0].equals(attackUnit) && pair[1].equals(defendUnit)) {
                return 1.2; // 克制
            }
            if (pair[1].equals(attackUnit) && pair[0].equals(defendUnit)) {
                return 0.8; // 被克
            }
        }
        
        // 策士被所有克
        if ("策".equals(attackUnit)) return 0.8;
        
        return 1.0;
    }
    
    /**
     * 计算伤害
     */
    public static int calculateDamage(int atk, int def, double counterBonus, double skillMult) {
        // 基础伤害 = (攻击 - 防御*0.5) * 克制系数 * 技能系数
        double base = (atk - def * 0.5);
        if (base < 10) base = 10; // 最小伤害
        
        int damage = (int)(base * counterBonus * skillMult);
        return Math.max(damage, 1);
    }
    
    /**
     * 获取兵种属性
     */
    public static java.util.Map<String, Integer> getUnitStats(String unitType) {
        return UNIT_STATS.getOrDefault(unitType, UNIT_STATS.get("步"));
    }
}
