package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SkillRequirementConfig {
    
    public static final Map<Integer, Map<String, Object>> SKILLS = new HashMap<>();
    
    static {
        // 普通技能（蓝色品质即可）
        SKILLS.put(1, Map.of("id", 1, "name", "天火降世", "level", 1, "quality", "blue", "cost", 100));
        SKILLS.put(2, Map.of("id", 2, "name", "雷霆万钧", "level", 1, "quality", "blue", "cost", 90));
        SKILLS.put(3, Map.of("id", 3, "name", "剑扫八荒", "level", 1, "quality", "blue", "cost", 80));
        SKILLS.put(4, Map.of("id", 4, "name", "裂地击", "level", 1, "quality", "blue", "cost", 70));
        SKILLS.put(5, Map.of("id", 5, "name", "狂风斩", "level", 1, "quality", "blue", "cost", 65));
        SKILLS.put(6, Map.of("id", 6, "name", "烈焰刀", "level", 1, "quality", "blue", "cost", 60));
        
        // 稀有技能（需要紫色）
        SKILLS.put(7, Map.of("id", 7, "name", "龙吸水", "level", 2, "quality", "purple", "cost", 120));
        SKILLS.put(8, Map.of("id", 8, "name", "穿云箭", "level", 2, "quality", "purple", "cost", 110));
        SKILLS.put(11, Map.of("id", 11, "name", "金刚护体", "level", 2, "quality", "purple", "cost", 150));
        SKILLS.put(21, Map.of("id", 21, "name", "回天术", "level", 2, "quality", "purple", "cost", 180));
        SKILLS.put(31, Map.of("id", 31, "name", "天雷镇魂", "level", 2, "quality", "purple", "cost", 160));
        
        // 史诗技能（需要橙色）
        SKILLS.put(13, Map.of("id", 13, "name", "铜墙铁壁", "level", 3, "quality", "orange", "cost", 200));
        SKILLS.put(22, Map.of("id", 22, "name", "春风化雨", "level", 3, "quality", "orange", "cost", 200));
        SKILLS.put(32, Map.of("id", 32, "name", "定身咒", "level", 3, "quality", "orange", "cost", 180));
        SKILLS.put(41, Map.of("id", 41, "name", "战神附体", "level", 3, "quality", "orange", "cost", 220));
        SKILLS.put(61, Map.of("id", 61, "name", "不屈意志", "level", 3, "quality", "orange", "cost", 180));
        
        // 传说技能（需要红色）
        SKILLS.put(23, Map.of("id", 23, "name", "枯木逢春", "level", 4, "quality", "red", "cost", 300));
        SKILLS.put(43, Map.of("id", 43, "name", "风林火山", "level", 4, "quality", "red", "cost", 350));
        SKILLS.put(44, Map.of("id", 44, "name", "亢龙有悔", "level", 4, "quality", "red", "cost", 320));
        SKILLS.put(64, Map.of("id", 64, "name", "吸血", "level", 4, "quality", "red", "cost", 350));
        SKILLS.put(65, Map.of("id", 65, "name", "洞察", "level", 4, "quality", "red", "cost", 320));
        
        // 神级技能（需要红将+特定条件）
        SKILLS.put(72, Map.of("id", 72, "name", "玉石俱焚", "level", 5, "quality", "red", "cost", 800));
        SKILLS.put(73, Map.of("id", 73, "name", "偷天换日", "level", 5, "quality", "red", "cost", 1000));
        SKILLS.put(74, Map.of("id", 74, "name", "召唤术", "level", 5, "quality", "red", "cost", 800));
        SKILLS.put(75, Map.of("id", 75, "name", "分身为三", "level", 5, "quality", "red", "cost", 900));
    }
    
    public Map<Integer, Map<String, Object>> getAll() { return SKILLS; }
    public Map<String, Object> get(Integer id) { return SKILLS.get(id); }
}
