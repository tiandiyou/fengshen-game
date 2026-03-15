package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 武将系统测试
 * 测试六维属性、伤害计算、成长系统
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
        
        // 验证六维属性
        assertEquals(180, p.getAtk());
        assertEquals(85, p.getIntelligence());
        assertEquals(90, p.getLead());
        assertEquals(95, p.getSpeed());
        assertEquals(30, p.getPolitics());
        assertEquals(95, p.getMilitary());
        assertEquals(70, p.getCharm());
    }
    
    @Test
    void testPhysicalDamageCalculation() {
        // 物理伤害 = 武力 - 统率
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        attacker.setGrowthAtk(8);
        
        // 敌方统率80
        int damage = attacker.calcPhysicalDamage(80);
        
        // 180 - 80 = 100
        assertEquals(100, damage);
        
        // 测试高防御
        damage = attacker.calcPhysicalDamage(200);
        // 180 - 200 = -20，但最低为1
        assertEquals(1, damage);
    }
    
    @Test
    void testMagicDamageCalculation() {
        // 法术伤害 = 智力 - 智力
        Partner attacker = new Partner();
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        attacker.setGrowthInt(7);
        
        // 敌方智力60
        int damage = attacker.calcMagicDamage(60);
        
        // 140 - 60 = 80
        assertEquals(80, damage);
        
        // 测试高法防
        damage = attacker.calcMagicDamage(200);
        // 140 - 200 = -60，但最低为1
        assertEquals(1, damage);
    }
    
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
        assertEquals(100 + (5-1) * 5, p.getCurrentAtk()); // 120
        assertEquals(80 + (5-1) * 3, p.getCurrentInt());  // 92
        assertEquals(70 + (5-1) * 2, p.getCurrentLead()); // 78
    }
    
    @Test
    void testLevelUpSystem() {
        Partner p = new Partner();
        p.setLevel(1);
        p.setExp(0);
        
        // 1级升2级需要100经验
        assertEquals(100, p.getExpForNextLevel());
        assertFalse(p.canLevelUp());
        
        // 增加100经验
        p.setExp(100);
        assertTrue(p.canLevelUp());
        
        p.levelUp();
        assertEquals(2, p.getLevel());
        assertEquals(0, p.getExp()); // 经验已消耗
        
        // 2级升3级需要200经验
        p.setExp(300); // 300 - 200 = 100剩余
        p.levelUp();
        assertEquals(3, p.getLevel());
        assertEquals(100, p.getExp());
    }
    
    @Test
    void testZhanliCalculation() {
        Partner p = new Partner();
        p.setAtk(180);
        p.setIntelligence(85);
        p.setLead(90);
        p.setMaxTroops(10000);
        p.setLevel(1);
        p.setGrowthAtk(8);
        p.setGrowthInt(4);
        p.setGrowthLead(5);
        
        // 战力 = 武力*2 + 智力*2 + 统率*2 + 兵量/100
        int expected = 180*2 + 85*2 + 90*2 + 10000/100;
        assertEquals(expected, p.getZhanli());
    }
    
    @Test
    void testStarSystem() {
        Partner p = new Partner();
        p.setStar(0);
        assertEquals(0, p.getStar());
        
        p.setStar(5);
        assertEquals(5, p.getStar());
        
        // 星级上限检查
        assertTrue(p.getStar() <= 5);
    }
    
    @Test
    void testHiddenRedPartner() {
        // 隐藏红将测试 - 品质显示为orange，实际为red
        Partner p = new Partner();
        p.setName("隐藏红将A");
        p.setQuality("red"); // 实际品质
        p.setAtk(200);
        p.setIntelligence(100);
        p.setLead(95);
        
        // 验证红将属性较高
        assertTrue(p.getAtk() >= 180);
        assertTrue(p.getLead() >= 85);
    }
}
