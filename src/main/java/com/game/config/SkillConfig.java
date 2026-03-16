package com.game.config;

import java.util.*;

/**
 * 战法配置 SLG核心战斗系统
 */
public class SkillConfig {
    
    public enum SkillType {
        ACTIVE, PASSIVE, COMMAND
    }
    
    public enum EffectType {
        DAMAGE, HEAL, BUFF, DEBUFF, CONTROL, PIERCE, COUNTER
    }
    
    public static final List<Map<String, Object>> SKILLS = Arrays.asList(
        // 伤害类
        Map.of("id", 1, "name", "突击", "type", SkillType.ACTIVE, "effect", EffectType.DAMAGE,
            "value", 120, "target", "单体", "desc", "普通攻击，伤害120%"),
        
        Map.of("id", 2, "name", "重击", "type", SkillType.ACTIVE, "effect", EffectType.DAMAGE,
            "value", 150, "target", "单体", "desc", "全力一击，伤害150%"),
        
        Map.of("id", 3, "name", "横扫", "type", SkillType.ACTIVE, "effect", EffectType.DAMAGE,
            "value", 80, "target", "群体", "desc", "攻击范围内所有敌人，伤害80%"),
        
        Map.of("id", 4, "name", "斩首", "type", SkillType.ACTIVE, "effect", EffectType.DAMAGE,
            "value", 200, "target", "单体", "desc", "针对主将，伤害200%，但只能使用1次"),
        
        Map.of("id", 5, "name", "穿透", "type", SkillType.ACTIVE, "effect", EffectType.PIERCE,
            "value", 100, "target", "单体", "desc", "无视防御造成伤害"),
        
        // 控制类
        Map.of("id", 6, "name", "眩晕", "type", SkillType.ACTIVE, "effect", EffectType.CONTROL,
            "value", 30, "target", "单体", "desc", "30%概率眩晕敌人1回合"),
        
        Map.of("id", 7, "name", "混乱", "type", SkillType.ACTIVE, "effect", EffectType.CONTROL,
            "value", 25, "target", "单体", "desc", "25%概率使敌人混乱1回合"),
        
        // 增益类
        Map.of("id", 8, "name", "激励", "type", SkillType.ACTIVE, "effect", EffectType.BUFF,
            "value", 20, "target", "自身", "desc", "提升自身20%攻击力，持续1回合"),
        
        Map.of("id", 9, "name", "护盾", "type", SkillType.ACTIVE, "effect", EffectType.BUFF,
            "value", 30, "target", "自身", "desc", "为自己套上30%生命值的护盾"),
        
        // 治疗
        Map.of("id", 10, "name", "急救", "type", SkillType.ACTIVE, "effect", EffectType.HEAL,
            "value", 80, "target", "友军", "desc", "治疗友军造成伤害的80%"),
        
        // 被动
        Map.of("id", 101, "name", "连击", "type", SkillType.PASSIVE, "effect", EffectType.DAMAGE,
            "value", 30, "target", "自身", "desc", "每次攻击后30%概率再次攻击"),
        
        Map.of("id", 102, "name", "反击", "type", SkillType.PASSIVE, "effect", EffectType.COUNTER,
            "value", 40, "target", "自身", "desc", "受到攻击时40%概率反击"),
        
        Map.of("id", 103, "name", "洞察", "type", SkillType.PASSIVE, "effect", EffectType.BUFF,
            "value", 100, "target", "自身", "desc", "免疫控制效果"),
        
        Map.of("id", 104, "name", "坚守", "type", SkillType.PASSIVE, "effect", EffectType.BUFF,
            "value", 15, "target", "自身", "desc", "提升15%防御"),
        
        // 指挥
        Map.of("id", 201, "name", "冲锋", "type", SkillType.COMMAND, "effect", EffectType.BUFF,
            "value", 10, "target", "全体", "desc", "全体攻击力+10%，战斗开始时生效"),
        
        Map.of("id", 202, "name", "铁壁", "type", SkillType.COMMAND, "effect", EffectType.BUFF,
            "value", 10, "target", "全体", "desc", "全体防御+10%，战斗开始时生效"),
        
        Map.of("id", 203, "name", "提速", "type", SkillType.COMMAND, "effect", EffectType.BUFF,
            "value", 5, "target", "全体", "desc", "全体速度+5，战斗开始时生效"),
        
        Map.of("id", 204, "name", "不屈", "type", SkillType.COMMAND, "effect", EffectType.BUFF,
            "value", 20, "target", "全体", "desc", "全体兵力最低+20%，战斗开始时生效")
    );
}
