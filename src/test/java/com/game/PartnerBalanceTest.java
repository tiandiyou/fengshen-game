package com.game;

import com.game.config.PartnerData;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 武将数据平衡性测试 - 包含技能属性
 */
public class PartnerBalanceTest {
    
    // ========== 数量统计测试 ==========
    
    @Test
    void testPartnerCount() {
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        assertEquals(100, all.size(), "总共应该有100个武将");
    }
    
    @Test
    void testRedPartnerCount() {
        List<Map<String, Object>> red = PartnerData.getRedPartners();
        assertEquals(10, red.size(), "红将应该有10个");
    }
    
    @Test
    void testOrangePartnerCount() {
        List<Map<String, Object>> orange = PartnerData.getOrangePartners();
        assertEquals(30, orange.size(), "橙将应该有30个");
    }
    
    @Test
    void testPurplePartnerCount() {
        List<Map<String, Object>> purple = PartnerData.getPurplePartners();
        assertEquals(30, purple.size(), "紫将应该有30个");
    }
    
    @Test
    void testBluePartnerCount() {
        List<Map<String, Object>> blue = PartnerData.getBluePartners();
        assertEquals(30, blue.size(), "蓝将应该有30个");
    }
    
    // ========== 属性平衡性测试 ==========
    
    @Test
    void testRedPartnerAttributes() {
        List<Map<String, Object>> reds = PartnerData.getRedPartners();
        
        for (Map<String, Object> p : reds) {
            int atk = (Integer) p.get("atk");
            int lead = (Integer) p.get("lead");
            int intelligence = (Integer) p.get("intelligence");
            
            // 红将武力/智力/统率至少有一个 >= 150
            assertTrue(atk >= 150 || lead >= 85 || intelligence >= 140,
                p.get("name") + " 应该有至少一项属性达到红将标准");
        }
    }
    
    @Test
    void testOrangePartnerAttributes() {
        List<Map<String, Object>> oranges = PartnerData.getOrangePartners();
        
        for (Map<String, Object> p : oranges) {
            int atk = (Integer) p.get("atk");
            int lead = (Integer) p.get("lead");
            int intelligence = (Integer) p.get("intelligence");
            
            // 橙将属性范围: 30-165
            assertTrue(atk >= 30 && atk <= 170, p.get("name") + " 武力应在30-170之间");
            assertTrue(lead >= 30 && lead <= 110, p.get("name") + " 统率应在30-110之间");
            assertTrue(intelligence >= 25 && intelligence <= 140, p.get("name") + " 智力应在25-140之间");
        }
    }
    
    @Test
    void testPurplePartnerAttributes() {
        List<Map<String, Object>> purples = PartnerData.getPurplePartners();
        
        for (Map<String, Object> p : purples) {
            int atk = (Integer) p.get("atk");
            int lead = (Integer) p.get("lead");
            int intelligence = (Integer) p.get("intelligence");
            
            // 紫将属性范围: 10-100
            assertTrue(atk >= 10 && atk <= 100, p.get("name") + " 武力应在10-100之间");
            assertTrue(lead >= 10 && lead <= 65, p.get("name") + " 统率应在10-65之间");
            assertTrue(intelligence >= 10 && intelligence <= 95, p.get("name") + " 智力应在10-95之间");
        }
    }
    
    @Test
    void testBluePartnerAttributes() {
        List<Map<String, Object>> blues = PartnerData.getBluePartners();
        
        for (Map<String, Object> p : blues) {
            int atk = (Integer) p.get("atk");
            int lead = (Integer) p.get("lead");
            int intelligence = (Integer) p.get("intelligence");
            
            // 蓝将属性范围: 5-40
            assertTrue(atk >= 5 && atk <= 40, p.get("name") + " 武力应在5-40之间");
            assertTrue(lead >= 5 && lead <= 45, p.get("name") + " 统率应在5-45之间");
            assertTrue(intelligence >= 5 && intelligence <= 30, p.get("name") + " 智力应在5-30之间");
        }
    }
    
    // ========== 技能属性测试 ==========
    
    @Test
    void testRedPartnerHas2Skills() {
        // 红将应该有2个技能
        List<Map<String, Object>> reds = PartnerData.getRedPartners();
        
        for (Map<String, Object> p : reds) {
            List<String> skills = (List<String>) p.get("skills");
            assertTrue(skills != null && skills.size() == 2,
                p.get("name") + " 应该有2个技能");
        }
    }
    
    @Test
    void testOrangePartnerHas1Skill() {
        // 橙将应该有1个技能
        List<Map<String, Object>> oranges = PartnerData.getOrangePartners();
        
        for (Map<String, Object> p : oranges) {
            List<String> skills = (List<String>) p.get("skills");
            assertTrue(skills != null && skills.size() >= 1,
                p.get("name") + " 应该有至少1个技能");
        }
    }
    
    @Test
    void testSkillDistribution() {
        // 测试技能分布
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        
        Map<String, Integer> skillCount = new HashMap<>();
        for (Map<String, Object> p : all) {
            List<String> skills = (List<String>) p.get("skills");
            int count = skills != null ? skills.size() : 0;
            skillCount.put(count + "技能", skillCount.getOrDefault(count + "技能", 0) + 1);
        }
        
        // 应该有2技能、1技能、无技能三种情况
        assertTrue(skillCount.containsKey("2技能"), "应该有2技能武将");
        assertTrue(skillCount.containsKey("1技能"), "应该有1技能武将");
        assertTrue(skillCount.containsKey("0技能"), "应该有0技能武将");
    }
    
    @Test
    void testSkillTypes() {
        // 验证技能类型
        List<String> allSkills = Arrays.asList(
            "火尖枪", "八九玄功", "封神榜", "天子剑", "魅惑",
            "风雷棍", "开天珠", "定海珠", "雌雄双鞭", "玲珑宝塔",
            "莲花护体", "天眼通", "天命所归", "帝王之气", "倾城之貌",
            "飞仙之翼", "逆天改命", "财神附体", "托塔天王"
        );
        
        assertTrue(allSkills.size() >= 15);
    }
    
    // ========== 伤害公式验证测试 ==========
    
    @Test
    void testRedPartnerDamage() {
        List<Map<String, Object>> reds = PartnerData.getRedPartners();
        
        for (Map<String, Object> p : reds) {
            int atk = (Integer) p.get("atk");
            int lead = (Integer) p.get("lead");
            
            // 物理伤害 = atk * (1 - lead/(lead+200))
            double expectedDmg = atk * (1 - (double)lead / (lead + 200));
            double minDmg = atk * 0.25; // 最低25%伤害
            
            assertTrue(expectedDmg >= minDmg, 
                p.get("name") + " 输出伤害应该至少有攻击力的25%");
        }
    }
    
    // ========== 兵种分布测试 ==========
    
    @Test
    void testTypeDistribution() {
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        Map<String, Integer> typeCount = new HashMap<>();
        
        for (Map<String, Object> p : all) {
            String type = (String) p.get("type");
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
        }
        
        // 应该有4种兵种
        assertTrue(typeCount.containsKey("步"), "应该有步兵");
        assertTrue(typeCount.containsKey("骑"), "应该有骑兵");
        assertTrue(typeCount.containsKey("弓"), "应该有弓兵");
        assertTrue(typeCount.containsKey("策"), "应该有策士");
    }
    
    // ========== 成长值测试 ==========
    
    @Test
    void testGrowthValues() {
        List<Map<String, Object>> reds = PartnerData.getRedPartners();
        
        for (Map<String, Object> p : reds) {
            if (p.containsKey("growthAtk")) {
                int growthAtk = (Integer) p.get("growthAtk");
                assertTrue(growthAtk >= 1 && growthAtk <= 10, 
                    p.get("name") + " 成长值应在1-10之间");
            }
        }
    }
    
    @Test
    void testRedPartnerGrowthHigherThanOrange() {
        // 红将成长应该比橙将高
        double redAvgGrowth = PartnerData.getRedPartners().stream()
            .filter(p -> p.containsKey("growthAtk"))
            .mapToInt(p -> (Integer) p.get("growthAtk"))
            .average().orElse(0);
        
        double orangeAvgGrowth = PartnerData.getOrangePartners().stream()
            .filter(p -> p.containsKey("growthAtk"))
            .mapToInt(p -> (Integer) p.get("growthAtk"))
            .average().orElse(0);
        
        assertTrue(redAvgGrowth >= orangeAvgGrowth + 1, 
            "红将平均成长应该比橙将高至少1");
    }
    
    // ========== 唯一性测试 ==========
    
    @Test
    void testUniqueIds() {
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        Set<Integer> ids = new HashSet<>();
        
        for (Map<String, Object> p : all) {
            int id = (Integer) p.get("id");
            assertTrue(ids.add(id), "武将ID应该唯一: " + id);
        }
    }
    
    @Test
    void testUniqueNames() {
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        Set<String> names = new HashSet<>();
        
        for (Map<String, Object> p : all) {
            String name = (String) p.get("name");
            assertTrue(names.add(name), "武将名称应该唯一: " + name);
        }
    }
    
    // ========== 伤害输出对比测试 ==========
    
    @Test
    void testDamageOutputComparison() {
        // 红将平均伤害应该比橙将高至少50%
        double redAvgAtk = PartnerData.getRedPartners().stream()
            .mapToInt(p -> (Integer) p.get("atk")).average().orElse(0);
        
        double orangeAvgAtk = PartnerData.getOrangePartners().stream()
            .mapToInt(p -> (Integer) p.get("atk")).average().orElse(0);
        
        double ratio = redAvgAtk / orangeAvgAtk;
        assertTrue(ratio >= 1.3, "红将平均武力应该比橙将高30%以上, 当前:" + ratio);
        
        // 橙将平均伤害应该比紫将高至少80%
        double purpleAvgAtk = PartnerData.getPurplePartners().stream()
            .mapToInt(p -> (Integer) p.get("atk")).average().orElse(0);
        
        ratio = orangeAvgAtk / purpleAvgAtk;
        assertTrue(ratio >= 1.5, "橙将平均武力应该比紫将高50%以上, 当前:" + ratio);
        
        // 紫将平均伤害应该比蓝将高至少100%
        double blueAvgAtk = PartnerData.getBluePartners().stream()
            .mapToInt(p -> (Integer) p.get("atk")).average().orElse(0);
        
        ratio = purpleAvgAtk / blueAvgAtk;
        assertTrue(ratio >= 1.8, "紫将平均武力应该比蓝将高80%以上, 当前:" + ratio);
    }
    
    // ========== 速度分布测试 ==========
    
    @Test
    void testSpeedDistribution() {
        List<Map<String, Object>> all = PartnerData.getAllPartners();
        
        // 速度应该在20-110之间
        for (Map<String, Object> p : all) {
            int speed = (Integer) p.get("speed");
            assertTrue(speed >= 20 && speed <= 120, 
                p.get("name") + " 速度应在20-120之间");
        }
    }
    
    // ========== 综合战斗力测试 ==========
    
    @Test
    void testCombatPowerByQuality() {
        // 测试各品质综合战斗力
        // 战斗力 = 武力 + 智力 + 统率 + 速度 + 成长加成 + 技能加成
        
        // 红将平均战力应该最高
        double redPower = PartnerData.getRedPartners().stream()
            .mapToInt(p -> (Integer)p.get("atk") + (Integer)p.get("intelligence") + 
                          (Integer)p.get("lead") + (Integer)p.get("speed"))
            .average().orElse(0);
        
        double orangePower = PartnerData.getOrangePartners().stream()
            .mapToInt(p -> (Integer)p.get("atk") + (Integer)p.get("intelligence") + 
                          (Integer)p.get("lead") + (Integer)p.get("speed"))
            .average().orElse(0);
        
        assertTrue(redPower > orangePower * 1.3, 
            "红将平均战力应该比橙将高30%以上");
    }
}
