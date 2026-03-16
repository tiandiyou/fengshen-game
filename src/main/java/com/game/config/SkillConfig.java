package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class SkillConfig {
    
    public static final Map<Integer, Map<String, Object>> SKILLS = new HashMap<>();
    
    static {
        // ===== 攻击类 =====
        SKILLS.put(1, Map.of("id", 1, "name", "天火降世", "type", "attack", "desc", "召唤天火降下，造成300%伤害", "cost", 100, "cd", 3));
        SKILLS.put(2, Map.of("id", 2, "name", "雷霆万钧", "type", "attack", "desc", "万道雷霆劈下，造成280%伤害", "cost", 90, "cd", 3));
        SKILLS.put(3, Map.of("id", 3, "name", "剑扫八荒", "type", "attack", "desc", "剑气横扫四方，造成250%伤害", "cost", 80, "cd", 2));
        SKILLS.put(4, Map.of("id", 4, "name", "裂地击", "type", "attack", "desc", "大地裂开震击，造成220%伤害", "cost", 70, "cd", 2));
        SKILLS.put(5, Map.of("id", 5, "name", "狂风斩", "type", "attack", "desc", "狂风刀刃斩击，造成210%伤害", "cost", 65, "cd", 2));
        SKILLS.put(6, Map.of("id", 6, "name", "烈焰刀", "type", "attack", "desc", "烈火刀锋劈砍，造成200%伤害", "cost", 60, "cd", 2));
        SKILLS.put(7, Map.of("id", 7, "name", "龙吸水", "type", "attack", "desc", "水龙卷吞噬敌，造成190%伤害", "cost", 55, "cd", 2));
        SKILLS.put(8, Map.of("id", 8, "name", "穿云箭", "type", "attack", "desc", "箭如流星穿云，造成180%伤害", "cost", 50, "cd", 1));
        
        // ===== 防御类 =====
        SKILLS.put(11, Map.of("id", 11, "name", "金刚护体", "type", "defense", "desc", "金刚不坏之身，免疫伤害3秒", "cost", 120, "cd", 5));
        SKILLS.put(12, Map.of("id", 12, "name", "绝对防御", "type", "defense", "desc", "绝对防御立场，减免80%伤害", "cost", 100, "cd", 4));
        SKILLS.put(13, Map.of("id", 13, "name", "铜墙铁壁", "type", "defense", "desc", "铜墙铁壁防线，减免60%伤害", "cost", 80, "cd", 3));
        SKILLS.put(14, Map.of("id", 14, "name", "土崩瓦解", "type", "defense", "desc", "土系护盾防御，减免50%伤害", "cost", 70, "cd", 3));
        SKILLS.put(15, Map.of("id", 15, "name", "水波护盾", "type", "defense", "desc", "水波护盾环绕，减免40%伤害", "cost", 60, "cd", 2));
        
        // ===== 辅助类 =====
        SKILLS.put(21, Map.of("id", 21, "name", "回天术", "type", "heal", "desc", "回天乏术，回复自身50%生命", "cost", 150, "cd", 5));
        SKILLS.put(22, Map.of("id", 22, "name", "春风化雨", "type", "heal", "desc", "春风化雨滋润，回复40%生命", "cost", 120, "cd", 4));
        SKILLS.put(23, Map.of("id", 23, "name", "枯木逢春", "type", "heal", "desc", "枯木逢春生发，回复30%生命", "cost", 100, "cd", 3));
        SKILLS.put(24, Map.of("id", 24, "name", "灵丹妙药", "type", "heal", "desc", "灵丹妙药医心，回复25%生命", "cost", 80, "cd", 3));
        
        // ===== 控制类 =====
        SKILLS.put(31, Map.of("id", 31, "name", "天雷镇魂", "type", "control", "desc", "天雷镇魂眩晕，眩晕敌人3秒", "cost", 140, "cd", 5));
        SKILLS.put(32, Map.of("id", 32, "name", "定身咒", "type", "control", "desc", "定身咒法封印，定身敌人2秒", "cost", 110, "cd", 4));
        SKILLS.put(33, Map.of("id", 33, "name", "冰封千里", "type", "control", "desc", "冰封千里冻僵，冰冻敌人2秒", "cost", 100, "cd", 4));
        SKILLS.put(34, Map.of("id", 34, "name", "迷魂阵", "type", "control", "desc", "迷魂阵中迷乱，混乱敌人2秒", "cost", 90, "cd", 3));
        SKILLS.put(35, Map.of("id", 35, "name", "石化之眼", "type", "control", "desc", "石化之眼凝视，石化敌人1.5秒", "cost", 80, "cd", 3));
        
        // ===== 增益类 =====
        SKILLS.put(41, Map.of("id", 41, "name", "战神附体", "type", "buff", "desc", "战神附体加持，攻击+50%", "cost", 130, "cd", 5));
        SKILLS.put(42, Map.of("id", 42, "name", "法天象地", "type", "buff", "desc", "法天象地之威，生命+50%", "cost", 130, "cd", 5));
        SKILLS.put(43, Map.of("id", 43, "name", "风林火山", "type", "buff", "desc", "风林火山之势，全属性+30%", "cost", 150, "cd", 6));
        SKILLS.put(44, Map.of("id", 44, "name", "亢龙有悔", "type", "buff", "desc", "亢龙有悔之力，暴击+40%", "cost", 120, "cd", 4));
        SKILLS.put(45, Map.of("id", 45, "name", "九霄天雷", "type", "buff", "desc", "九霄天雷祝福，速度+40%", "cost", 110, "cd", 4));
        
        // ===== 减益类 =====
        SKILLS.put(51, Map.of("id", 51, "name", "削弱咒", "type", "debuff", "desc", "削弱咒语降灵，敌人攻击-30%", "cost", 80, "cd", 3));
        SKILLS.put(52, Map.of("id", 52, "name", "破甲术", "type", "debuff", "desc", "破甲术破防御，敌人防御-30%", "cost", 80, "cd", 3));
        SKILLS.put(53, Map.of("id", 53, "name", "减速符", "type", "debuff", "desc", "减速符咒封印，敌人速度-30%", "cost", 70, "cd", 3));
        SKILLS.put(54, Map.of("id", 54, "name", "破胆怒吼", "type", "debuff", "desc", "破胆怒吼威慑，敌人暴击-30%", "cost", 70, "cd", 3));
        
        // ===== 被动类 =====
        SKILLS.put(61, Map.of("id", 61, "name", "不屈意志", "type", "passive", "desc", "不屈意志激发，血量低于30%时防御+50%", "cost", 100, "cd", 0));
        SKILLS.put(62, Map.of("id", 62, "name", "反击风暴", "type", "passive", "desc", "反击风暴开启，受击时20%概率反击", "cost", 90, "cd", 0));
        SKILLS.put(63, Map.of("id", 63, "name", "连击", "type", "passive", "desc", "连击之刃锋利，攻击时15%概率连击", "cost", 110, "cd", 0));
        SKILLS.put(64, Map.of("id", 64, "name", "吸血", "type", "passive", "desc", "吸血之力吸取，攻击时10%吸血", "cost", 120, "cd", 0));
        SKILLS.put(65, Map.of("id", 65, "name", "洞察", "type", "passive", "desc", "洞察之眼明锐，暴击伤害+30%", "cost", 100, "cd", 0));
        SKILLS.put(66, Map.of("id", 66, "name", "铁壁", "type", "passive", "desc", "铁壁防御坚固，受到暴击伤害-30%", "cost", 90, "cd", 0));
        SKILLS.put(67, Map.of("id", 67, "name", "疾风", "type", "passive", "desc", "疾风步履如飞，闪避概率+15%", "cost", 100, "cd", 0));
        SKILLS.put(68, Map.of("id", 68, "name", "穿透", "type", "passive", "desc", "穿透之力锋锐，攻击忽视防御20%", "cost", 110, "cd", 0));
        
        // ===== 特殊类 =====
        SKILLS.put(71, Map.of("id", 71, "name", "移花接木", "type", "special", "desc", "移花接木换命，与队友交换生命", "cost", 200, "cd", 8));
        SKILLS.put(72, Map.of("id", 72, "name", "玉石俱焚", "type", "special", "desc", "玉石俱焚同归于尽，自身死亡对敌全伤", "cost", 300, "cd", 10));
        SKILLS.put(73, Map.of("id", 73, "name", "偷天换日", "type", "special", "desc", "偷天换日逆天改命，复活并回满血量", "cost", 500, "cd", 15));
        SKILLS.put(74, Map.of("id", 74, "name", "召唤术", "type", "special", "desc", "召唤术法召唤，召唤一个分身协助作战", "cost", 250, "cd", 8));
        SKILLS.put(75, Map.of("id", 75, "name", "分身为三", "type", "special", "desc", "分身为三之术，分成三个分身作战", "cost", 350, "cd", 10));
    }
    
    public Map<Integer, Map<String, Object>> getAllSkills() {
        return SKILLS;
    }
    
    public Map<String, Object> getSkill(Integer id) {
        return SKILLS.get(id);
    }
}
