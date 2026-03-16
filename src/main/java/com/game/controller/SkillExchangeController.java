package com.game.controller;

import com.game.config.SkillConfig;
import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/skill")
@CrossOrigin
public class SkillExchangeController {
    
    @Autowired
    private SkillConfig skillConfig;
    
    @Autowired
    private PlayerRepository playerRepo;
    
    // 获取技能列表
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Map<String, Object>> skills = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> e : skillConfig.getAllSkills().entrySet()) {
            Map<String, Object> s = new HashMap<>(e.getValue());
            skills.add(s);
        }
        return Map.of("success", true, "skills", skills);
    }
    
    // 获取技能详情
    @GetMapping("/detail")
    public Map<String, Object> detail(Integer id) {
        Map<String, Object> skill = skillConfig.getSkill(id);
        if (skill == null) {
            return Map.of("success", false, "message", "技能不存在");
        }
        return Map.of("success", true, "skill", skill);
    }
    
    // 兑换技能
    @PostMapping("/exchange")
    public Map<String, Object> exchange(Long playerId, Integer skillId) {
        Map<String, Object> skill = skillConfig.getSkill(skillId);
        if (skill == null) {
            return Map.of("success", false, "message", "技能不存在");
        }
        
        Player p = playerRepo.findById(playerId).orElse(null);
        if (p == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        
        int cost = (int) skill.get("cost");
        if (p.getGold() < cost) {
            return Map.of("success", false, "message", "金币不足，需要" + cost);
        }
        
        p.setGold(p.getGold() - cost);
        playerRepo.save(p);
        
        return Map.of("success", true, "message", "兑换成功", "skill", skill.get("name"), "cost", cost);
    }
    
    // 技能类型列表
    @GetMapping("/types")
    public Map<String, Object> types() {
        return Map.of(
            "success", true,
            "types", Arrays.asList(
                Map.of("id", "attack", "name", "攻击型", "icon", "⚔️"),
                Map.of("id", "defense", "name", "防御型", "icon", "🛡️"),
                Map.of("id", "heal", "name", "治疗型", "icon", "💚"),
                Map.of("id", "control", "name", "控制型", "icon", "⛓️"),
                Map.of("id", "buff", "name", "增益型", "icon", "✨"),
                Map.of("id", "debuff", "name", "减益型", "icon", "🔻"),
                Map.of("id", "passive", "name", "被动型", "icon", "💫"),
                Map.of("id", "special", "name", "特殊型", "icon", "🔮")
            )
        );
    }
}
