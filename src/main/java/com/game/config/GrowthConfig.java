package com.game.config;

import java.util.Map;

/**
 * 武将成长系统配置
 */
public class GrowthConfig {
    
    // 升级配置
    public static final Map<String, Integer> LEVEL_UP_COST = Map.of(
        "lingqi", 10  // 每级消耗灵气 = 等级 * 10
    );
    
    // 每级属性成长
    public static final Map<String, Integer> LEVEL_UP_BONUS = Map.of(
        "hp", 100,      // 生命+100/级
        "atk", 10,      // 攻击+10/级
        "speed", 1      // 速度+1/级
    );
    
    // 升星配置: 星级 -> 需要同名卡数量
    public static final Map<Integer, Integer> STAR_UP_COST = Map.of(
        1, 0,   // 1星->2星需要0张（初始1星）
        2, 1,   // 2星需要1张同名
        3, 2,   // 3星需要2张
        4, 3,   // 4星需要3张
        5, 4,   // 5星需要4张
        6, 5,   // 6星需要5张
        7, 6,   // 7星需要6张
        8, 8,   // 8星需要8张
        9, 10,  // 9星需要10张
        10, 12  // 10星需要12张
    );
    
    // 升星属性加成 (%)
    public static final Map<Integer, Integer> STAR_BONUS = Map.of(
        1, 0,
        2, 10,
        3, 20,
        4, 30,
        5, 40,
        6, 50,
        7, 60,
        8, 75,
        9, 90,
        10, 110
    );
    
    // 突破配置: 品质 -> 突破消耗
    public static final Map<String, Map<String, Object>> BREAKTHROUGH = Map.of(
        "blue", Map.of(
            "cost", 500,
            "costType", "lingqi",
            "bonus", Map.of("atk", 5, "hp", 50)
        ),
        "purple", Map.of(
            "cost", 1000,
            "costType", "lingqi",
            "bonus", Map.of("atk", 10, "hp", 100)
        ),
        "orange", Map.of(
            "cost", 2000,
            "costType", "lingqi",
            "bonus", Map.of("atk", 15, "hp", 150)
        ),
        "red", Map.of(
            "cost", 5000,
            "costType", "lingqi",
            "bonus", Map.of("atk", 25, "hp", 250)
        )
    );
    
    // 品质压制 (PVP时高品对低品的伤害加成)
    public static final Map<String, Integer> QUALITY_BONUS = Map.of(
        "red", 50,      // 红打橙+50%
        "orange", 30,   // 橙打紫+30%
        "purple", 20,   // 紫打蓝+20%
        "blue", 0
    );
}
