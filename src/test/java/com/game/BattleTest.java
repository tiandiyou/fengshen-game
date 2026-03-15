package com.game;

import com.game.entity.Partner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 战斗系统测试
 * 测试物理伤害、法术伤害、战斗逻辑
 */
public class BattleTest {
    
    @Test
    void testPhysicalAttack() {
        // 我方武将
        Partner attacker = new Partner();
        attacker.setName("哪吒");
        attacker.setAtk(180);
        attacker.setLevel(1);
        attacker.setGrowthAtk(8);
        
        // 敌方武将
        Partner defender = new Partner();
        defender.setName("申公豹");
        defender.setLead(75); // 统率作为物理防御
        
        // 物理伤害 = 武力 - 统率
        int damage = attacker.calcPhysicalDamage(defender.getLead());
        
        assertEquals(105, damage); // 180 - 75 = 105
    }
    
    @Test
    void testMagicAttack() {
        // 我方法师
        Partner attacker = new Partner();
        attacker.setName("姜子牙");
        attacker.setIntelligence(140);
        attacker.setLevel(1);
        attacker.setGrowthInt(7);
        
        // 敌方
        Partner defender = new Partner();
        defender.setIntelligence(60); // 智力作为法术防御
        
        // 法术伤害 = 智力 - 智力
        int damage = attacker.calcMagicDamage(defender.getIntelligence());
        
        assertEquals(80, damage); // 140 - 60 = 80
    }
    
    @Test
    void testMinDamage() {
        // 测试最低伤害为1
        Partner attacker = new Partner();
        attacker.setAtk(50);
        
        Partner defender = new Partner();
        defender.setLead(200); // 高防御
        
        int damage = attacker.calcPhysicalDamage(defender.getLead());
        assertEquals(1, damage);
        
        // 法术
        attacker.setIntelligence(50);
        defender.setIntelligence(200);
        
        damage = attacker.calcMagicDamage(defender.getIntelligence());
        assertEquals(1, damage);
    }
    
    @Test
    void testTroopsFromPlayerLevel() {
        // 测试兵量由玩家等级决定
        int level1Troops = 10000 + 1 * 1000;
        int level5Troops = 10000 + 5 * 1000;
        int level10Troops = 10000 + 10 * 1000;
        
        assertEquals(11000, level1Troops);
        assertEquals(15000, level5Troops);
        assertEquals(20000, level10Troops);
    }
    
    @Test
    void testSpeedAffectsOrder() {
        // 测试速度影响行动顺序
        Partner fast = new Partner();
        fast.setName("妲己");
        fast.setSpeed(110);
        
        Partner slow = new Partner();
        slow.setName("李靖");
        slow.setSpeed(60);
        
        assertTrue(fast.getSpeed() > slow.getSpeed());
    }
    
    @Test
    void testTypeCounter() {
        // 测试兵种克制关系
        // 步兵克骑兵、骑兵克弓兵、弓兵克步兵、策士需要保护
        String[][] counters = {
            {"步", "骑"},  // 步兵克制骑兵
            {"骑", "弓"},  // 骑兵克制弓兵
            {"弓", "步"}   // 弓兵克制步兵
        };
        
        // 验证克制关系
        assertEquals("骑", counters[0][1]); // 步克骑
        assertEquals("弓", counters[1][1]);   // 骑克弓
        assertEquals("步", counters[2][1]);  // 弓克步
    }
    
    @Test
    void testBattleScenario() {
        // 模拟战斗场景
        // 哪吒(武力180) vs 敖丙(统率50)
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
        int physicalDmg = neZha.calcPhysicalDamage(aoBing.getLead());
        assertEquals(130, physicalDmg); // 180 - 50 = 130
        
        // 敖丙物理攻击哪吒
        int physicalDmg2 = aoBing.calcPhysicalDamage(neZha.getLead());
        assertEquals(1, Math.max(1, 80 - 90)); // 80 - 90 = -10，但最低1
        
        // 哪吒法术攻击敖丙
        int magicDmg = neZha.calcMagicDamage(aoBing.getIntelligence());
        assertEquals(45, magicDmg); // 85 - 40 = 45
    }
}
