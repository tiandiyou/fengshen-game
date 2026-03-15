package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 武将平衡性测试 v2.1 - 等级成长+星级系统
 */
public class PartnerBalanceV2Test {
    
    // ========== 品质等级上限测试 ==========
    
    @Test
    void testRedPartnerMaxLevel() {
        Partner p = new Partner();
        p.setQuality("red");
        p.setMaxLevel(80);
        
        assertEquals(80, p.getMaxLevel());
    }
    
    @Test
    void testOrangePartnerMaxLevel() {
        Partner p = new Partner();
        p.setQuality("orange");
        p.setMaxLevel(60);
        
        assertEquals(60, p.getMaxLevel());
    }
    
    @Test
    void testPurplePartnerMaxLevel() {
        Partner p = new Partner();
        p.setQuality("purple");
        p.setMaxLevel(40);
        
        assertEquals(40, p.getMaxLevel());
    }
    
    @Test
    void testBluePartnerMaxLevel() {
        Partner p = new Partner();
        p.setQuality("blue");
        p.setMaxLevel(30);
        
        assertEquals(30, p.getMaxLevel());
    }
    
    // ========== 星级系统测试 ==========
    
    @Test
    void testStarRange() {
        Partner p = new Partner();
        
        for (int star = 1; star <= 6; star++) {
            p.setStar(star);
            assertTrue(p.getStar() >= 1 && p.getStar() <= 6);
        }
    }
    
    @Test
    void testStarBonusProgression() {
        Partner p = new Partner();
        
        // 验证星级加成递增
        double[] expected = {0, 0.1, 0.25, 0.45, 0.70, 1.0};
        
        for (int i = 1; i <= 6; i++) {
            p.setStar(i);
            assertEquals(expected[i-1], p.getStarBonus(), 0.01, "星級" + i + "加成");
        }
    }
    
    // ========== 成长值范围测试 ==========
    
    @Test
    void testRedGrowthRange() {
        // 红将成长: 武力3.0, 智力2.5, 统率2.0, 速度1.5
        Partner p = new Partner();
        p.setQuality("red");
        p.setAtk(160);
        p.setGrowthAtk(3);
        p.setGrowthInt(3);
        p.setGrowthLead(2);
        p.setGrowthSpeed(2);
        
        assertEquals(3, p.getGrowthAtk());
        assertEquals(3, p.getGrowthInt());
    }
    
    @Test
    void testOrangeGrowthRange() {
        // 橙将成长应该低于红将
        Partner p = new Partner();
        p.setQuality("orange");
        p.setGrowthAtk(2);
        p.setGrowthInt(2);
        p.setGrowthLead(1);
        
        assertTrue(p.getGrowthAtk() < 3);
    }
    
    // ========== 战力平衡测试 ==========
    
    @Test
    void testMaxLevelMaxStarZhanli() {
        // 红将满级满星战力
        Partner red = new Partner();
        red.setQuality("red");
        red.setAtk(160);
        red.setIntelligence(75);
        red.setLead(80);
        red.setSpeed(90);
        red.setGrowthAtk(3);
        red.setGrowthInt(2);
        red.setGrowthLead(2);
        red.setGrowthSpeed(2);
        red.setMaxLevel(80);
        red.setLevel(80);
        red.setStar(6);
        
        // 橙将满级满星战力
        Partner orange = new Partner();
        orange.setQuality("orange");
        orange.setAtk(130);
        orange.setIntelligence(45);
        orange.setLead(68);
        orange.setSpeed(78);
        orange.setGrowthAtk(2);
        orange.setGrowthInt(1);
        orange.setGrowthLead(1);
        orange.setGrowthSpeed(1);
        orange.setMaxLevel(60);
        orange.setLevel(60);
        orange.setStar(6);
        
        int redZhanli = red.getZhanli();
        int orangeZhanli = orange.getZhanli();
        
        // 红将战力应该更高
        assertTrue(redZhanli > orangeZhanli);
        
        // 但差距应该在4倍以内
        double ratio = (double) redZhanli / orangeZhanli;
        assertTrue(ratio < 4.0, "满级满星战力差距应小于4倍，当前:" + ratio);
    }
    
    @Test
    void test1StarZhanliDifference() {
        // 1星时差距应该更小
        Partner red = new Partner();
        red.setAtk(160);
        red.setIntelligence(75);
        red.setLead(80);
        red.setSpeed(90);
        red.setLevel(1);
        red.setStar(1);
        
        Partner orange = new Partner();
        orange.setAtk(130);
        orange.setIntelligence(45);
        orange.setLead(68);
        orange.setSpeed(78);
        orange.setLevel(1);
        orange.setStar(1);
        
        double ratio = (double) red.getZhanli() / orange.getZhanli();
        
        // 1星时差距应该小于2倍
        assertTrue(ratio < 2.0, "1星战力差距应小于2倍");
    }
    
    // ========== 兵量计算测试 ==========
    
    @Test
    void testTroopsByLevel() {
        Partner p = new Partner();
        
        // 兵量 = 10000 + 等级×500
        assertEquals(10500, p.getCurrentTroops()); // 1级
        
        p.setLevel(30);
        assertEquals(25000, p.getCurrentTroops());
        
        p.setLevel(60);
        assertEquals(40000, p.getCurrentTroops());
        
        p.setLevel(80);
        assertEquals(50000, p.getCurrentTroops());
    }
    
    // ========== 技能测试 ==========
    
    @Test
    void testSkillCount() {
        Partner red = new Partner();
        red.setSkill1("火尖枪");
        red.setSkill2("疾风");
        
        Partner orange = new Partner();
        orange.setSkill1("遁龙桩");
        
        Partner blue = new Partner();
        
        // 红将应该有2个技能
        assertNotNull(red.getSkill1());
        assertNotNull(red.getSkill2());
        
        // 橙将应该有1个技能
        assertNotNull(orange.getSkill1());
        assertNull(orange.getSkill2());
        
        // 蓝将无技能
        assertNull(blue.getSkill1());
    }
    
    // ========== 伤害公式测试 ==========
    
    @Test
    void testPhysicalDamageFormula() {
        // 物理伤害 = 武力 × (1 - 统率/(统率+200))
        Partner p = new Partner();
        p.setAtk(100);
        p.setLevel(1);
        p.setStar(1);
        
        // 统率0时，伤害=100
        assertEquals(100, p.calcPhysicalDamage(0));
        
        // 统率200时，伤害=100×0.5=50
        assertEquals(50, p.calcPhysicalDamage(200));
        
        // 统率400时，伤害=100×0.33=33
        assertEquals(33, p.calcPhysicalDamage(400));
        
        // 统率1000时，伤害最低=1
        assertEquals(1, p.calcPhysicalDamage(1000));
    }
    
    @Test
    void testMagicDamageFormula() {
        // 法术伤害 = 智力 × (1 - 智力/(智力+200))
        Partner p = new Partner();
        p.setIntelligence(100);
        p.setLevel(1);
        p.setStar(1);
        
        // 敌方智力0时，伤害=100
        assertEquals(100, p.calcMagicDamage(0));
        
        // 敌方智力200时，伤害=100×0.5=50
        assertEquals(50, p.calcMagicDamage(200));
    }
    
    // ========== 经验系统测试 ==========
    
    @Test
    void testExpRequirement() {
        Partner p = new Partner();
        p.setLevel(1);
        p.setMaxLevel(80);
        
        // 每级所需经验 = 等级×100
        assertEquals(100, p.getExpForNextLevel()); // 1→2
        
        p.setLevel(10);
        assertEquals(1000, p.getExpForNextLevel()); // 10→11
        
        p.setLevel(40);
        assertEquals(4000, p.getExpForNextLevel()); // 40→41
    }
    
    // ========== 速度分布测试 ==========
    
    @Test
    void testSpeedByQuality() {
        Partner red = new Partner();
        red.setSpeed(90);
        
        Partner blue = new Partner();
        blue.setSpeed(30);
        
        // 红将速度应该明显高于蓝将
        assertTrue(red.getCurrentSpeed() > blue.getCurrentSpeed() * 2);
    }
}
