package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 武将系统测试 v2.1 - 等级成长+星级系统
 */
public class PartnerLevelStarTest {
    
    // ========== 创建测试武将 ==========
    
    private Partner createRedPartner() {
        Partner p = new Partner();
        p.setName("哪吒");
        p.setQuality("red");
        p.setType("骑");
        p.setAtk(160);  // 基础武力
        p.setIntelligence(75);
        p.setLead(80);
        p.setSpeed(90);
        p.setGrowthAtk(3);
        p.setGrowthInt(2);
        p.setGrowthLead(2);
        p.setGrowthSpeed(2);
        p.setMaxLevel(80);
        p.setLevel(1);
        p.setStar(1);
        p.setSkill1("火尖枪");
        p.setSkill2("疾风");
        return p;
    }
    
    private Partner createOrangePartner() {
        Partner p = new Partner();
        p.setName("金吒");
        p.setQuality("orange");
        p.setType("骑");
        p.setAtk(130);
        p.setIntelligence(45);
        p.setLead(68);
        p.setSpeed(78);
        p.setGrowthAtk(2);
        p.setGrowthInt(2);
        p.setGrowthLead(1);
        p.setGrowthSpeed(1);
        p.setMaxLevel(60);
        p.setLevel(1);
        p.setStar(1);
        p.setSkill1("遁龙桩");
        return p;
    }
    
    // ========== 星级系统测试 ==========
    
    @Test
    void testStarBonus() {
        Partner p = createRedPartner();
        
        // 1星无加成
        p.setStar(1);
        assertEquals(0.0, p.getStarBonus(), 0.01);
        
        // 2星10%
        p.setStar(2);
        assertEquals(0.1, p.getStarBonus(), 0.01);
        
        // 3星25%
        p.setStar(3);
        assertEquals(0.25, p.getStarBonus(), 0.01);
        
        // 6星100%
        p.setStar(6);
        assertEquals(1.0, p.getStarBonus(), 0.01);
    }
    
    @Test
    void testStarPromotion() {
        Partner p = createRedPartner();
        
        assertEquals(1, p.getStar());
        assertEquals(2, p.getStarCost()); // 升2星需要2个
        
        p.promoteStar();
        assertEquals(2, p.getStar());
        
        // 继续升到6星
        for (int i = 0; i < 4; i++) {
            p.promoteStar();
        }
        assertEquals(6, p.getStar());
        assertEquals(0, p.getStarCost()); // 6星满
    }
    
    // ========== 等级系统测试 ==========
    
    @Test
    void testLevelUp() {
        Partner p = createRedPartner();
        
        assertEquals(1, p.getLevel());
        assertEquals(100, p.getExpForNextLevel()); // 2级需要100经验
        
        // 经验不足无法升级
        assertFalse(p.canLevelUp());
        
        // 增加足够经验
        p.setExp(150);
        assertTrue(p.canLevelUp());
        
        p.levelUp();
        assertEquals(2, p.getLevel());
        assertEquals(50, p.getExp()); // 150-100=50
        
        // 2级升3级需要200经验
        assertEquals(200, p.getExpForNextLevel());
    }
    
    @Test
    void testMaxLevel() {
        Partner p = createOrangePartner();
        p.setMaxLevel(60);
        
        // 升到满级
        for (int i = 0; i < 59; i++) {
            p.setExp(p.getExpForNextLevel() + 100);
            p.levelUp();
        }
        
        assertEquals(60, p.getLevel());
        
        // 满级后无法升级
        p.setExp(10000);
        assertFalse(p.canLevelUp());
    }
    
    // ========== 属性计算测试 ==========
    
    @Test
    void testLevelGrowth() {
        Partner p = createRedPartner();
        
        // 1级武力 = 160
        assertEquals(160, p.getCurrentAtk());
        
        // 40级武力 = 160 + (40-1) × 3 = 277
        p.setLevel(40);
        assertEquals(277, p.getCurrentAtk());
        
        // 80级武力 = 160 + (80-1) × 3 = 397
        p.setLevel(80);
        assertEquals(397, p.getCurrentAtk());
    }
    
    @Test
    void testStarBonusOnAttribute() {
        Partner p = createRedPartner();
        p.setLevel(1);
        
        // 1星: 160 × 1.0 = 160
        p.setStar(1);
        assertEquals(160, p.getCurrentAtk());
        
        // 6星: 160 × 2.0 = 320
        p.setStar(6);
        assertEquals(320, p.getCurrentAtk());
    }
    
    @Test
    void testLevelAndStarCombined() {
        Partner p = createRedPartner();
        
        // 哪吒 80级 6星
        p.setLevel(80);
        p.setStar(6);
        
        // 武力 = (160 + 79×3) × 2 = 397 × 2 = 794
        assertEquals(794, p.getCurrentAtk());
        
        // 智力 = (75 + 79×2) × 2 = 233 × 2 = 466
        assertEquals(466, p.getCurrentInt());
        
        // 统率 = (80 + 79×2) × 2 = 238 × 2 = 476
        assertEquals(476, p.getCurrentLead());
    }
    
    // ========== 兵量计算测试 ==========
    
    @Test
    void testTroopsCalculation() {
        Partner p = createRedPartner();
        
        // 1级兵量 = 10000 + 1×500 = 10500
        assertEquals(10500, p.getCurrentTroops());
        
        // 40级兵量 = 10000 + 40×500 = 30000
        p.setLevel(40);
        assertEquals(30000, p.getCurrentTroops());
        
        // 80级兵量 = 10000 + 80×500 = 50000
        p.setLevel(80);
        assertEquals(50000, p.getCurrentTroops());
    }
    
    // ========== 战力计算测试 ==========
    
    @Test
    void testZhanliCalculation() {
        Partner p = createRedPartner();
        
        // 1级1星战力
        // = (160+75+80)×2 + 90 + 10500/100 + 100(2技能)
        // = 630 + 90 + 105 + 100 = 925
        assertTrue(p.getZhanli() >= 900 && p.getZhanli() <= 1000);
        
        // 80级6星战力应该大幅提升
        p.setLevel(80);
        p.setStar(6);
        
        // 战力应该超过2000
        assertTrue(p.getZhanli() >= 2000);
    }
    
    @Test
    void testZhanliDifferenceByQuality() {
        Partner red = createRedPartner();
        red.setLevel(80);
        red.setStar(6);
        
        Partner orange = createOrangePartner();
        orange.setLevel(60);
        orange.setStar(6);
        
        // 红将战力应该比橙将高
        assertTrue(red.getZhanli() > orange.getZhanli());
        
        // 但差距应该控制在合理范围 (不超过3倍)
        double ratio = (double) red.getZhanli() / orange.getZhanli();
        assertTrue(ratio < 3.0, "战力差距应小于3倍");
    }
    
    // ========== 伤害计算测试 ==========
    
    @Test
    void testPhysicalDamageWithLevelAndStar() {
        Partner attacker = createRedPartner();
        attacker.setLevel(40);
        attacker.setStar(3); // 25%加成
        
        // 40级武力 = 160 + 39×3 = 277
        // 3星加成后 = 277 × 1.25 = 346
        int atk = attacker.getCurrentAtk();
        
        // 敌方统率50
        // 伤害 = 346 × (1 - 50/250) = 276
        int damage = attacker.calcPhysicalDamage(50);
        
        assertTrue(damage >= 200);
    }
    
    @Test
    void testMagicDamageWithLevelAndStar() {
        Partner attacker = createRedPartner();
        attacker.setLevel(80);
        attacker.setStar(6);
        
        // 智力 = (75 + 79×2) × 2 = 233 × 2 = 466
        int intVal = attacker.getCurrentInt();
        
        // 敌方智力60
        // 伤害 = 466 × (1 - 60/260) = 358
        int damage = attacker.calcMagicDamage(60);
        
        assertTrue(damage >= 300);
    }
    
    // ========== 经验系统测试 ==========
    
    @Test
    void testExpSystem() {
        Partner p = createRedPartner();
        
        // 1→2级需要100经验
        // 2→3级需要200经验
        // 3→4级需要300经验
        
        p.setExp(600); // 足够升3级
        p.addExp(0);
        
        assertEquals(3, p.getLevel());
        assertEquals(0, p.getExp()); // 600 - 100 - 200 - 300 = 0
    }
    
    // ========== 橙色将测试 ==========
    
    @Test
    void testOrangePartnerMaxLevel() {
        Partner p = createOrangePartner();
        
        assertEquals(60, p.getMaxLevel());
        
        // 尝试升到60级
        p.setLevel(60);
        p.setExp(10000);
        
        // 60级满级后无法升级
        assertFalse(p.canLevelUp());
    }
    
    @Test
    void testOrangePartnerGrowth() {
        Partner p = createOrangePartner();
        
        // 1级武力 = 130
        assertEquals(130, p.getCurrentAtk());
        
        // 60级武力 = 130 + 59×2 = 248
        p.setLevel(60);
        assertEquals(248, p.getCurrentAtk());
        
        // 6星加成后 = 248 × 2 = 496
        p.setStar(6);
        assertEquals(496, p.getCurrentAtk());
    }
}
