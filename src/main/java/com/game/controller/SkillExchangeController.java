package com.game.controller;

import com.game.config.SkillExchangeConfig;
import com.game.entity.Partner;
import com.game.entity.PlayerSkill;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/skill")
@CrossOrigin
public class SkillExchangeController {
    
    @Autowired
    private SkillExchangeConfig skillConfig;
    
    @Autowired
    private PlayerSkillRepository playerSkillRepo;
    
    @Autowired
    private PartnerRepository partnerRepo;
    
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Map<String, Object>> skills = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> e : skillConfig.getAll().entrySet()) {
            skills.add(new HashMap<>(e.getValue()));
        }
        return Map.of("success", true, "skills", skills);
    }
    
    @GetMapping("/detail")
    public Map<String, Object> detail(int id) {
        Map<String, Object> skill = skillConfig.get(id);
        if (skill == null) return Map.of("success", false, "message", "技能不存在");
        return Map.of("success", true, "skill", skill);
    }
    
    @GetMapping("/my")
    public Map<String, Object> mySkills(long playerId) {
        List<PlayerSkill> skills = playerSkillRepo.findByPlayerId(playerId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (PlayerSkill ps : skills) {
            Map<String, Object> skill = skillConfig.get(ps.getSkillId());
            if (skill != null) {
                Map<String, Object> m = new HashMap<>(skill);
                m.put("stars", ps.getStars());
                m.put("bonus", SkillExchangeConfig.getStarBonus(ps.getStars()));
                result.add(m);
            }
        }
        return Map.of("success", true, "skills", result);
    }
    
    @PostMapping("/exchange")
    public Map<String, Object> exchange(long playerId, int skillId, long heroId) {
        Map<String, Object> skill = skillConfig.get(skillId);
        if (skill == null) return Map.of("success", false, "message", "技能不存在");
        
        Optional<PlayerSkill> existing = playerSkillRepo.findByPlayerIdAndSkillId(playerId, skillId);
        if (existing.isPresent()) {
            return Map.of("success", false, "message", "已有该技能");
        }
        
        if (heroId > 0) {
            partnerRepo.deleteById(heroId);
        }
        
        PlayerSkill ps = new PlayerSkill();
        ps.setPlayerId(playerId);
        ps.setSkillId(skillId);
        ps.setStars(1);
        ps.setObtainTime(System.currentTimeMillis());
        ps.setUpgradeTime(System.currentTimeMillis());
        playerSkillRepo.save(ps);
        
        return Map.of("success", true, "message", "技能兑换成功", "skill", skill.get("name"), "stars", 1);
    }
    
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(long playerId, int skillId, long heroId) {
        Optional<PlayerSkill> existing = playerSkillRepo.findByPlayerIdAndSkillId(playerId, skillId);
        if (!existing.isPresent()) {
            return Map.of("success", false, "message", "未拥有该技能");
        }
        
        PlayerSkill ps = existing.get();
        if (ps.getStars() >= 6) {
            return Map.of("success", false, "message", "已达到最高星级");
        }
        
        if (heroId > 0) {
            partnerRepo.deleteById(heroId);
        }
        
        ps.setStars(ps.getStars() + 1);
        ps.setUpgradeTime(System.currentTimeMillis());
        playerSkillRepo.save(ps);
        
        double bonus = SkillExchangeConfig.getStarBonus(ps.getStars());
        
        return Map.of("success", true, "message", "升级成功", "stars", ps.getStars(), "bonus", bonus);
    }
    
    @GetMapping("/heroes")
    public Map<String, Object> heroes(long playerId) {
        List<Partner> partners = partnerRepo.findByPlayerId(playerId);
        return Map.of("success", true, "heroes", partners);
    }
}
