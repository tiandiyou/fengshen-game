package com.game.config;

/**
 * 阵营加成配置
 */
public class FactionBonus {
    
    // 阵营基础加成（同阵营武将全体属性+X%）
    public static final int FACTION_BONUS_BASE = 5; // 5%
    
    // 阵营高级加成（同阵营武将>=3人时）
    public static final int FACTION_BONUS_ADVANCED = 10; // 10%
    
    // 阵营加成类型
    public enum BonusType {
        ATK,      // 攻击
        HP,       // 生命
        DEF,      // 防御
        SPEED,    // 速度
        CRIT,     // 暴击
        ALL       // 全属性
    }
    
    // 阵营加成配置
    public static final java.util.Map<String, BonusType> FACTION_BONUS_TYPE = 
        java.util.Map.of(
            "阐教", BonusType.ATK,     // 阐教攻击加成
            "截教", BonusType.HP,      // 截教生命加成
            "商朝", BonusType.DEF,     // 商朝防御加成
            "周朝", BonusType.CRIT     // 周朝暴击加成
        );
    
    // 阵营名称
    public static final java.util.List<String> FACTIONS = 
        java.util.List.of("阐教", "截教", "商朝", "周朝");
}
