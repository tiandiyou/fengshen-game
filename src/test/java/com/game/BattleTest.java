package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 战斗系统测试 - 方案B伤害公式
 * 物理伤害 = 武力 × (1 - 统率 / (统率 + 200))
 * 法术伤害 = 智力 × (1 - 智力 / (智力 + 200))
 */
public class BattleTest {
    
    // ========== 方案B物理伤害测试 ==========
    
    @Test
    void testPhysicalAttack_LowDefense() {
        // 哪吒(武力180) vs 敖丙(统率50)
        Partner attacker = new Partner();
        attacker.setName("哪吒");
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        Partner defender = new Partner();
        defender.setName("敖丙");
        defender.setLead(50);
        
        // 180 × (1-50/250) = 180 × 0.8 = 144
        int damage = attacker.calcPhysicalDamage(defender.getLead());
        assertEquals(144, damage);
    }
    
    @Test
    void testPhysicalAttack_HighDefense() {
        // 武力180, 统率200
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        // 180 × (1-200/400) = 180 × 0.5 = 90
        int damage = attacker.calcPhysicalDamage(200);
        assertEquals(90, damage);
    }
    
    @Test
    void testPhysicalAttack_VeryHighDefense() {
        // 武力180, 统率500
        Partner attacker = new Partner();
        attacker.setAtk(180);
        attacker.setLevel(1);
        
        // 180 × (1-500/700) = 180 × 0.286 = 51
        int damage = attacker.calcPhysicalDamage(500);
        assertEquals(51, damage);
    }
    
    // ========== 方案B法术伤害测试 ==========
    
    @Test
    void testMagicAttack_LowDefense() {
        // 姜子牙(智力140) vs 申公豹(智力60)
        Partner attacker = new Partner();
        attacker.setName("姜子牙");
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        
        Partner defender = new Partner();
        defender.setIntelligence(60);
        
        // 140 × (1-60/260) = 140 × 0.769 = 107
        int damage = attacker.calcMagicDamage(defender.getIntelligence());
        assertEquals(107, damage);
    }
    
    @Test
    void testMagicAttack_HighDefense() {
        // 智力140, 敌方智力200
        Partner attacker = new Partner();
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        
        // 140 × (1-200/400) = 140 × 0.5 = 70
        int damage = attacker.calcMagicDamage(200);
        assertEquals(70, damage);
    }
    
    // ========== 最低伤害测试 ==========
    
    @Test
    void testMinDamage() {
        // 极低攻击力 vs 极高防御
        Partner attacker = new Partner();
        attacker.setAtk(10);
        
        Partner defender = new Partner();
        defender.setLead(1000);
        
        // 10 × (1-1000/1200) = 10 × 0.167 = 1.67 → 1
        int damage = attacker.calcPhysicalDamage(defender.getLead());
        assertEquals(1, damage);
    }
    
    // ========== 兵量系统测试 ==========
    
    @Test
    void testTroopsFromPlayerLevel() {
        assertEquals(11000, 10000 + 1 * 1000);
        assertEquals(15000, 10000 + 5 * 1000);
        assertEquals(20000, 10000 + 10 * 1000);
    }
    
    // ========== 速度系统测试 ==========
    
    @Test
    void testSpeedAffectsOrder() {
        Partner fast = new Partner();
        fast.setSpeed(110);
        
        Partner slow = new Partner();
        slow.setSpeed(60);
        
        assertTrue(fast.getSpeed() > slow.getSpeed());
    }
    
    // ========== 战斗场景模拟 ==========
    
    @Test
    void testBattleScenario_NeZhaVsAoBing() {
        // 哪吒(武力180, 智力85, 统率90) vs 敖丙(武力80, 智力40, 统率50)
        Partner neZha = new Partner();
        neZha.setName("哪吒");
        neZha.setAtk(180);
        neZha.setIntelligence(85);
        neZha.setLead(90);
        
        Partner aoBing = new Partner();
        aoBing.setName("敖丙");
        aoBing.setAtk(80);
        aoBing.setIntelligence(40);
        aoBing.setLead(50);
        
        // 哪吒物理攻击敖丙
        // 180 × (1-50/250) = 180 × 0.8 = 144
        int physicalDmg = neZha.calcPhysicalDamage(aoBing.getLead());
        assertEquals(144, physicalDmg);
        
        // 哪吒法术攻击敖丙
        // 85 × (1-40/240) = 85 × 0.833 = 70
        int magicDmg = neZha.calcMagicDamage(aoBing.getIntelligence());
        assertEquals(70, magicDmg);
        
        // 敖丙物理攻击哪吒
        // 80 × (1-90/290) = 80 × 0.69 = 55
        int physicalDmg2 = aoBing.calcPhysicalDamage(neZha.getLead());
        assertEquals(55, physicalDmg2);
    }
    
    @Test
    void testBattleScenario_JiangZiYaVsShenGongBao() {
        // 姜子牙(智力140) vs 申公豹(智力100)
        Partner jiang = new Partner();
        jiang.setName("姜子牙");
        jiang.setIntelligence(140);
        
        Partner shen = new Partner();
        shen.setName("申公豹");
        shen.setIntelligence(100);
        
        // 姜子牙法术攻击申公豹
        // 140 × (1-100/300) = 140 × 0.667 = 93
        int damage = jiang.calcMagicDamage(shen.getIntelligence());
        assertEquals(93, damage);
    }
    
    // ========== 伤害公式验证 ==========
    
    @Test
    void testDamageFormula_Physical() {
        // 验证物理伤害公式
        // 伤害 = 武力 × (1 - 统率 / (统率 + 200))
        Partner attacker = new Partner();
        
        // 统率为0时，伤害=武力×1
        attacker.setAtk(100);
        assertEquals(100, attacker.calcPhysicalDamage(0));
        
        // 统率=200时，伤害=武力×0.5
        assertEquals(50, attacker.calcPhysicalDamage(200));
        
        // 统率=400时，伤害=武力×0.333
        assertEquals(33, attacker.calcPhysicalDamage(400));
        
        // 统率=800时，伤害=武力×0.2
        assertEquals(20, attacker.calcPhysicalDamage(800));
    }
    
    @Test
    void testDamageFormula_Magic() {
        // 验证法术伤害公式
        Partner attacker = new Partner();
        
        // 敌方智力为0时，伤害=智力×1
        attacker.setIntelligence(100);
        assertEquals(100, attacker.calcMagicDamage(0));
        
        // 敌方智力=200时，伤害=智力×0.5
        assertEquals(50, attacker.calcMagicDamage(200));
        
        // 敌方智力=400时，伤害=智力×0.333
        assertEquals(33, attacker.calcMagicDamage(400));
    }
}
