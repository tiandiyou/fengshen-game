package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 武将系统测试 - 方案B伤害公式
 * 物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
 * 法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
 */
public class PartnerTest {
    
    @Test
    void testPartnerCreation() {
        Partner p = new Partner();
        p.setName("哪吒");
        p.setIcon("🔥");
        p.setQuality("red");
        p.setType("骑");
        p.setAtk(180);
        p.setIntelligence(85);
        p.setLead(90);
        p.setSpeed(95);
        p.setMaxTroops(10000);
        p.setGrowthAtk(8);
        p.setGrowthInt(4);
        p.setGrowthLead(5);
        
        assertEquals("哪吒", p.getName());
        assertEquals("red", p.getQuality());
        assertEquals("骑", p.getType());
    }
    
    @Test
    void testSixDimensionalAttributes() {
        Partner p = new Partner();
        p.setAtk(180);
        p.setIntelligence(85);
        p.setLead(90);
        p.setSpeed(95);
        p.setPolitics(30);
        p.setMilitary(95);
        p.setCharm(70);
        
        assertEquals(180, p.getAtk());
        assertEquals(85, p.getIntelligence());
        assertEquals(90, p.getLead());
        assertEquals(95, p.getSpeed());
    }
    
    // ========== 方案B物理伤害测试 ==========
    
    @Test
    void testPhysicalDamage_LowDefense() {
        // 武力180, 统率50 → 180 × (1-50/250) = 180 × 0.8 = 144
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        attacker.setGrowthAtk(0); // 固定值
        
        int damage = attacker.calcPhysicalDamage(50);
        assertEquals(144, damage);
    }
    
    @Test
    void testPhysicalDamage_MediumDefense() {
        // 武力180, 统率180 → 180 × (1-180/380) = 180 × 0.53 = 95
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        int damage = attacker.calcPhysicalDamage(180);
        assertEquals(95, damage);
    }
    
    @Test
    void testPhysicalDamage_HighDefense() {
        // 武力180, 统率500 → 180 × (1-500/700) = 180 × 0.29 = 52
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        int damage = attacker.calcPhysicalDamage(500);
        assertEquals(52, damage);
    }
    
    @Test
    void testPhysicalDamage_ZeroDefense() {
        // 武力180, 统率0 → 180 × (1-0/200) = 180 × 1 = 180
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        int damage = attacker.calcPhysicalDamage(0);
        assertEquals(180, damage);
    }
    
    // ========== 方案B法术伤害测试 ==========
    
    @Test
    void testMagicDamage_LowDefense() {
        // 智力140, 敌方智力60 → 140 × (1-60/260) = 140 × 0.77 = 107
        Partner attacker = new Partner();
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        
        int damage = attacker.calcMagicDamage(60);
        assertEquals(107, damage);
    }
    
    @Test
    void testMagicDamage_HighDefense() {
        // 智力140, 敌方智力200 → 140 × (1-200/400) = 140 × 0.5 = 70
        Partner attacker = new Partner();
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        
        int damage = attacker.calcMagicDamage(200);
        assertEquals(70, damage);
    }
    
    @Test
    void testMagicDamage_ZeroDefense() {
        // 智力140, 敌方智力0 → 140 × (1-0/200) = 140
        Partner attacker = new Partner();
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        
        int damage = attacker.calcMagicDamage(0);
        assertEquals(140, damage);
    }
    
    // ========== 成长系统测试 ==========
    
    @Test
    void testGrowthSystem() {
        Partner p = new Partner();
        p.setAtk(100);
        p.setIntelligence(80);
        p.setLead(70);
        p.setLevel(1);
        p.setGrowthAtk(5);
        p.setGrowthInt(3);
        p.setGrowthLead(2);
        
        // 1级属性
        assertEquals(100, p.getCurrentAtk());
        assertEquals(80, p.getCurrentInt());
        assertEquals(70, p.getCurrentLead());
        
        // 5级属性
        p.setLevel(5);
        assertEquals(120, p.getCurrentAtk()); // 100 + (5-1) * 5
        assertEquals(92, p.getCurrentInt());  // 80 + (5-1) * 3
        assertEquals(78, p.getCurrentLead()); // 70 + (5-1) * 2
    }
    
    @Test
    void testLevelUpSystem() {
        Partner p = new Partner();
        p.setLevel(1);
        p.setExp(0);
        
        assertEquals(100, p.getExpForNextLevel());
        assertFalse(p.canLevelUp());
        
        p.setExp(100);
        assertTrue(p.canLevelUp());
        
        p.levelUp();
        assertEquals(2, p.getLevel());
        assertEquals(0, p.getExp());
    }
    
    @Test
    void testZhanliCalculation() {
        Partner p = new Partner();
        p.setAtk(180);
        p.setIntelligence(85);
        p.setLead(90);
        p.setMaxTroops(10000);
        p.setLevel(1);
        
        int expected = 180*2 + 85*2 + 90*2 + 10000/100;
        assertEquals(expected, p.getZhanli());
    }
    
    @Test
    void testHiddenRedPartner() {
        Partner p = new Partner();
        p.setName("隐藏红将A");
        p.setQuality("red");
        p.setAtk(200);
        p.setIntelligence(100);
        p.setLead(95);
        
        assertTrue(p.getAtk() >= 180);
    }
}
