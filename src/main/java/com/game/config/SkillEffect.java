package com.game.config;

import java.util.*;

/**
 * 技能效果配置
 */
public class SkillEffect {
    private String type;        // 效果类型
    private int value;          // 效果值
    private int duration;       // 持续回合(0=即时)
    private String description;  // 描述
    
    public SkillEffect(String type, int value, int duration, String description) {
        this.type = type;
        this.value = value;
        this.duration = duration;
        this.description = description;
    }
    
    // ========== 常用技能效果 ==========
    
    // 物理伤害提升
    public static SkillEffect physicalDamage(int value) {
        return new SkillEffect("physical_damage", value, 0, "物理伤害+" + value + "%");
    }
    
    // 法术伤害提升
    public static SkillEffect magicDamage(int value) {
        return new SkillEffect("magic_damage", value, 0, "法术伤害+" + value + "%");
    }
    
    // 石化(无法行动)
    public static SkillEffect stone(int duration) {
        return new SkillEffect("stone", 0, duration, "石化 " + duration + " 回合");
    }
    
    // 流血(持续伤害)
    public static SkillEffect bleed(int value, int duration) {
        return new SkillEffect("bleed", value, duration, "每回合损失 " + value + " 兵量");
    }
    
    // 治疗
    public static SkillEffect heal(int value) {
        return new SkillEffect("heal", value, 0, "恢复 " + value + " 兵量");
    }
    
    // 护盾
    public static SkillEffect shield(int value, int duration) {
        return new SkillEffect("shield", value, duration, "护盾吸收 " + value + " 伤害");
    }
    
    // 沉默(无法释放技能)
    public static SkillEffect silence(int duration) {
        return new SkillEffect("silence", 0, duration, "沉默 " + duration + " 回合");
    }
    
    // 攻击提升
    public static SkillEffect atkUp(int value) {
        return new SkillEffect("atk_up", value, 0, "攻击力+" + value + "%");
    }
    
    // 防御提升
    public static SkillEffect defUp(int value) {
        return new SkillEffect("def_up", value, 0, "防御力+" + value + "%");
    }
    
    // 速度提升
    public static SkillEffect speedUp(int value) {
        return new SkillEffect("speed_up", value, 0, "速度+" + value);
    }
    
    // 暴击率提升
    public static SkillEffect critRate(int value) {
        return new SkillEffect("crit_rate", value, 0, "暴击率+" + value + "%");
    }
    
    // 闪避提升
    public static SkillEffect dodge(int value) {
        return new SkillEffect("dodge", value, 0, "闪避率+" + value + "%");
    }
    
    // ========== Getters ==========
    public String getType() { return type; }
    public int getValue() { return value; }
    public int getDuration() { return duration; }
    public String getDescription() { return description; }
}
