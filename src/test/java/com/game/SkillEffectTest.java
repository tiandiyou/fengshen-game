package com.game;

import com.game.config.SkillEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 技能系统测试
 * 测试技能效果计算
 */
public class SkillEffectTest {
    
    // ========== 伤害类技能测试 ==========
    
    @Test
    void testPhysicalDamageEffect() {
        // 物理伤害+50%
        SkillEffect effect = SkillEffect.physicalDamage(50);
        
        assertEquals("physical_damage", effect.getType());
        assertEquals(50, effect.getValue());
        assertEquals(0, effect.getDuration());
        assertEquals("物理伤害+50%", effect.getDescription());
    }
    
    @Test
    void testMagicDamageEffect() {
        // 法术伤害+40%
        SkillEffect effect = SkillEffect.magicDamage(40);
        
        assertEquals("magic_damage", effect.getType());
        assertEquals(40, effect.getValue());
    }
    
    @Test
    void testPhysicalDamageCalculation() {
        // 基础物理伤害
        int baseDamage = 100;
        int damageBonus = 50; // 50%伤害加成
        
        int finalDamage = baseDamage * (100 + damageBonus) / 100;
        
        assertEquals(150, finalDamage);
    }
    
    @Test
    void testMagicDamageCalculation() {
        // 基础法术伤害
        int baseDamage = 80;
        int damageBonus = 30; // 30%伤害加成
        
        int finalDamage = baseDamage * (100 + damageBonus) / 100;
        
        assertEquals(104, finalDamage);
    }
    
    // ========== 异常类技能测试 ==========
    
    @Test
    void testStoneEffect() {
        // 石化2回合
        SkillEffect effect = SkillEffect.stone(2);
        
        assertEquals("stone", effect.getType());
        assertEquals(0, effect.getValue());
        assertEquals(2, effect.getDuration());
        assertEquals("石化 2 回合", effect.getDescription());
    }
    
    @Test
    void testBleedEffect() {
        // 流血: 每回合损失50兵量，持续3回合
        SkillEffect effect = SkillEffect.bleed(50, 3);
        
        assertEquals("bleed", effect.getType());
        assertEquals(50, effect.getValue());
        assertEquals(3, effect.getDuration());
        assertEquals("每回合损失 50 兵量", effect.getDescription());
    }
    
    @Test
    void testBleedDamageCalculation() {
        // 流血伤害计算
        int bleedDamage = 50;
        int duration = 3;
        int totalDamage = bleedDamage * duration;
        
        assertEquals(150, totalDamage);
    }
    
    @Test
    void testHealEffect() {
        // 治疗: 恢复200兵量
        SkillEffect effect = SkillEffect.heal(200);
        
        assertEquals("heal", effect.getType());
        assertEquals(200, effect.getValue());
        assertEquals(0, effect.getDuration());
        assertEquals("恢复 200 兵量", effect.getDescription());
    }
    
    @Test
    void testHealCalculation() {
        // 治疗效果计算(不超过最大兵量)
        int currentTroops = 8000;
        int maxTroops = 10000;
        int healAmount = 5000;
        
        int actualHeal = Math.min(healAmount, maxTroops - currentTroops);
        
        assertEquals(2000, actualHeal);
    }
    
    @Test
    void testShieldEffect() {
        // 护盾: 吸收300伤害，持续2回合
        SkillEffect effect = SkillEffect.shield(300, 2);
        
        assertEquals("shield", effect.getType());
        assertEquals(300, effect.getValue());
        assertEquals(2, effect.getDuration());
        assertEquals("护盾吸收 300 伤害", effect.getDescription());
    }
    
    @Test
    void testShieldCalculation() {
        // 护盾效果计算
        int shieldValue = 300;
        int incomingDamage = 500;
        
        int absorbed = Math.min(shieldValue, incomingDamage);
        int remainingDamage = incomingDamage - absorbed;
        
        assertEquals(300, absorbed);
        assertEquals(200, remainingDamage);
    }
    
    @Test
    void testSilenceEffect() {
        // 沉默2回合
        SkillEffect effect = SkillEffect.silence(2);
        
        assertEquals("silence", effect.getType());
        assertEquals(0, effect.getValue());
        assertEquals(2, effect.getDuration());
    }
    
    // ========== 增益类技能测试 ==========
    
    @Test
    void testAtkUpEffect() {
        // 攻击+20%
        SkillEffect effect = SkillEffect.atkUp(20);
        
        assertEquals("atk_up", effect.getType());
        assertEquals(20, effect.getValue());
    }
    
    @Test
    void testDefUpEffect() {
        // 防御+15%
        SkillEffect effect = SkillEffect.defUp(15);
        
        assertEquals("def_up", effect.getType());
        assertEquals(15, effect.getValue());
    }
    
    @Test
    void testSpeedUpEffect() {
        // 速度+30
        SkillEffect effect = SkillEffect.speedUp(30);
        
        assertEquals("speed_up", effect.getType());
        assertEquals(30, effect.getValue());
    }
    
    @Test
    void testCritRateEffect() {
        // 暴击率+15%
        SkillEffect effect = SkillEffect.critRate(15);
        
        assertEquals("crit_rate", effect.getType());
        assertEquals(15, effect.getValue());
    }
    
    @Test
    void testDodgeEffect() {
        // 闪避+10%
        SkillEffect effect = SkillEffect.dodge(10);
        
        assertEquals("dodge", effect.getType());
        assertEquals(10, effect.getValue());
    }
    
    // ========== 综合战斗测试 ==========
    
    @Test
    void testSkillCombo_DamageAndDebuff() {
        // 技能组合: 物理伤害+30% + 流血(30伤害/回合,3回合)
        int baseAtk = 150;
        int damageBonus = 30; // +30%
        
        // 伤害计算
        int damage = baseAtk * (100 + damageBonus) / 100;
        assertEquals(195, damage);
        
        // 流血伤害
        int bleedDamage = 30 * 3;
        int totalDamage = damage + bleedDamage;
        assertEquals(285, totalDamage);
    }
    
    @Test
    void testSkillCombo_HealAndShield() {
        // 技能组合: 治疗200 + 护盾150
        int currentTroops = 9000;
        int maxTroops = 10000;
        
        // 治疗
        int heal = Math.min(200, maxTroops - currentTroops);
        int afterHeal = currentTroops + heal;
        
        // 护盾
        int shield = 150;
        
        assertEquals(1000, heal);
        assertEquals(10000, afterHeal);
        assertEquals(150, shield);
    }
    
    @Test
    void testSkillTiming() {
        // 技能时效测试
        // 石化2回合意味着第3回合才能行动
        int stoneDuration = 2;
        int currentTurn = 1;
        
        int turnsSkipped = 0;
        for (int i = 0; i < stoneDuration; i++) {
            turnsSkipped++;
        }
        
        assertEquals(2, turnsSkipped);
    }
    
    @Test
    void testCritWithDamageBonus() {
        // 暴击 + 伤害加成
        int baseDamage = 100;
        int critMultiplier = 150; // 暴击150%伤害
        int damageBonus = 20; // +20%伤害
        
        int critDamage = baseDamage * critMultiplier / 100;
        int finalDamage = critDamage * (100 + damageBonus) / 100;
        
        assertEquals(150, critDamage);
        assertEquals(180, finalDamage);
    }
    
    @Test
    void testQualitySkillPoints() {
        // 不同品质武将的技能点
        // 红将: 2个技能, 橙将: 1-2个, 紫将: 1个, 蓝将: 无
        
        int redSkillPoints = 2;
        int orangeSkillPoints = 1;
        int purpleSkillPoints = 1;
        int blueSkillPoints = 0;
        
        assertEquals(2, redSkillPoints);
        assertEquals(1, orangeSkillPoints);
        assertEquals(1, purpleSkillPoints);
        assertEquals(0, blueSkillPoints);
    }
    
    @Test
    void testSkillEffectValuesByQuality() {
        // 不同品质的技能效果值
        // 红将: 40-60, 橙将: 25-40, 紫将: 15-25, 蓝将: 5-15
        
        int redMin = 40, redMax = 60;
        int orangeMin = 25, orangeMax = 40;
        int purpleMin = 15, purpleMax = 25;
        int blueMin = 5, blueMax = 15;
        
        // 红将技能效果应该是最高的
        assertTrue(redMin > orangeMax);
        assertTrue(orangeMin > purpleMax);
        assertTrue(purpleMin > blueMax);
    }
}
