package com.game.controller;

import com.game.config.GameData;
import com.game.entity.Skill;
import com.game.mapper.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/skill")
@CrossOrigin
public class SkillController {
    @Autowired
    private SkillRepository skillRepository;
    
    // 获取所有战法
    @GetMapping("/list")
    public Map<String, Object> getSkills() {
        List<Map<String, Object>> skills = GameData.SKILLS;
        return Map.of("success", true, "skills", skills);
    }
    
    // 获取玩家拥有的战法
    @GetMapping("/my")
    public Map<String, Object> getMySkills(@RequestParam Long playerId) {
        List<Skill> skills = skillRepository.findAll();
        return Map.of("success", true, "skills", skills);
    }
    
    // 初始化战法数据(管理员用)
    @PostMapping("/init")
    public Map<String, Object> initSkills() {
        // 如果数据库为空，则初始化
        if (skillRepository.count() == 0) {
            List<Skill> skills = createDefaultSkills();
            skillRepository.saveAll(skills);
            return Map.of("success", true, "message", "战法数据初始化完成");
        }
        return Map.of("success", true, "message", "战法数据已存在");
    }
    
    private List<Skill> createDefaultSkills() {
        List<Skill> skills = new ArrayList<>();
        
        // 主动战法
        Skill s1 = new Skill();
        s1.setSkillId(1);
        s1.setName("火尖枪");
        s1.setIcon("🔱");
        s1.setType("主动");
        s1.setEffectType("伤害");
        s1.setEffectValue(150);
        s1.setCooldown(2);
        s1.setCost(30);
        s1.setTargetType("单体");
        s1.setDescription("火尖枪攻击单个目标，造成150%武力伤害");
        skills.add(s1);
        
        Skill s2 = new Skill();
        s2.setSkillId(2);
        s2.setName("八九玄功");
        s2.setIcon("🌀");
        s2.setType("主动");
        s2.setEffectType("伤害");
        s2.setEffectValue(140);
        s2.setCooldown(2);
        s2.setCost(25);
        s2.setTargetType("单体");
        s2.setDescription("杨戬绝技，造成140%武力伤害");
        skills.add(s2);
        
        // 被动战法
        Skill s3 = new Skill();
        s3.setSkillId(3);
        s3.setName("莲花护体");
        s3.setIcon("🪷");
        s3.setType("被动");
        s3.setEffectType("增益");
        s3.setEffectValue(10);
        s3.setCooldown(0);
        s3.setCost(0);
        s3.setTargetType("自身");
        s3.setDescription("永久提升10%防御");
        skills.add(s3);
        
        // 指挥战法
        Skill s4 = new Skill();
        s4.setSkillId(4);
        s4.setName("鼓舞全军");
        s4.setIcon("🎺");
        s4.setType("指挥");
        s4.setEffectType("增益");
        s4.setEffectValue(15);
        s4.setCooldown(0);
        s4.setCost(0);
        s4.setTargetType("全体友军");
        s4.setDescription("战斗开始时，全队提升15%攻击力");
        skills.add(s4);
        
        return skills;
    }
}
