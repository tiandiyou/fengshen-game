package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SkillExchangeConfig {
    
    public static final Map<Integer, Map<String, Object>> EXCHANGE = new HashMap<>();
    
    static {
        EXCHANGE.put(1, Map.of("id", 1, "name", "天火降世", "level", 1, "quality", "blue", 
            "requireHeroes", Arrays.asList("蓝将任意")));
        EXCHANGE.put(2, Map.of("id", 2, "name", "雷霆万钧", "level", 1, "quality", "blue",
            "requireHeroes", Arrays.asList("蓝将任意")));
        EXCHANGE.put(3, Map.of("id", 3, "name", "剑扫八荒", "level", 1, "quality", "blue",
            "requireHeroes", Arrays.asList("蓝将任意")));
        EXCHANGE.put(4, Map.of("id", 4, "name", "裂地击", "level", 1, "quality", "blue",
            "requireHeroes", Arrays.asList("蓝将任意")));
        EXCHANGE.put(5, Map.of("id", 5, "name", "狂风斩", "level", 2, "quality", "purple",
            "requireHeroes", Arrays.asList("紫将任意")));
        EXCHANGE.put(6, Map.of("id", 6, "name", "烈焰刀", "level", 2, "quality", "purple",
            "requireHeroes", Arrays.asList("紫将任意")));
        EXCHANGE.put(7, Map.of("id", 7, "name", "龙吸水", "level", 2, "quality", "purple",
            "requireHeroes", Arrays.asList("紫将任意")));
        EXCHANGE.put(8, Map.of("id", 8, "name", "穿云箭", "level", 3, "quality", "orange",
            "requireHeroes", Arrays.asList("橙将任意")));
        EXCHANGE.put(9, Map.of("id", 9, "name", "金刚护体", "level", 3, "quality", "orange",
            "requireHeroes", Arrays.asList("橙将任意")));
        EXCHANGE.put(10, Map.of("id", 10, "name", "回天术", "level", 3, "quality", "orange",
            "requireHeroes", Arrays.asList("橙将任意")));
        EXCHANGE.put(11, Map.of("id", 11, "name", "绝对防御", "level", 4, "quality", "red",
            "requireHeroes", Arrays.asList("橙将x2", "红将x1")));
        EXCHANGE.put(12, Map.of("id", 12, "name", "战神附体", "level", 4, "quality", "red",
            "requireHeroes", Arrays.asList("橙将x2", "红将x1")));
        EXCHANGE.put(13, Map.of("id", 13, "name", "风林火山", "level", 4, "quality", "red",
            "requireHeroes", Arrays.asList("橙将x2", "红将x1")));
        EXCHANGE.put(14, Map.of("id", 14, "name", "偷天换日", "level", 5, "quality", "red",
            "requireHeroes", Arrays.asList("燃灯道人", "通天教主")));
        EXCHANGE.put(15, Map.of("id", 15, "name", "玉石俱焚", "level", 5, "quality", "red",
            "requireHeroes", Arrays.asList("纣王", "姬发")));
        EXCHANGE.put(16, Map.of("id", 16, "name", "召唤术", "level", 5, "quality", "red",
            "requireHeroes", Arrays.asList("通天教主", "多宝道人")));
        EXCHANGE.put(17, Map.of("id", 17, "name", "分身为三", "level", 5, "quality", "red",
            "requireHeroes", Arrays.asList("多宝道人", "金灵圣母")));
    }
    
    public static final Map<Integer, Map<String, Object>> UPGRADE = new HashMap<>();
    
    static {
        UPGRADE.put(2, Map.of("stars", 2, "quality", "blue", "requireHeroes", Arrays.asList("蓝将任意")));
        UPGRADE.put(3, Map.of("stars", 3, "quality", "purple", "requireHeroes", Arrays.asList("紫将任意")));
        UPGRADE.put(4, Map.of("stars", 4, "quality", "orange", "requireHeroes", Arrays.asList("橙将任意")));
        UPGRADE.put(5, Map.of("stars", 5, "quality", "red", "requireHeroes", Arrays.asList("红将任意")));
        UPGRADE.put(6, Map.of("stars", 6, "quality", "red", "requireHeroes", Arrays.asList("红将x2")));
    }
    
    public static double getStarBonus(int stars) {
        return 1.0 + (stars - 1) * 0.05;
    }
    
    public Map<Integer, Map<String, Object>> getAll() { return EXCHANGE; }
    public Map<String, Object> get(int id) { return EXCHANGE.get(id); }
    public Map<String, Object> getUpgrade(int stars) { return UPGRADE.get(stars); }
}
