package com.game.controller;

import com.game.config.SkillRequirementConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/skill")
@CrossOrigin
public class SkillExchangeController {
    
    @Autowired
    private SkillRequirementConfig skillConfig;
    
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Map<String, Object>> skills = new ArrayList<>();
        for (Map.Entry<Integer, Map<String, Object>> e : skillConfig.getAll().entrySet()) {
            skills.add(new HashMap<>(e.getValue()));
        }
        return Map.of("success", true, "skills", skills);
    }
    
    @GetMapping("/detail")
    public Map<String, Object> detail(Integer id) {
        Map<String, Object> skill = skillConfig.get(id);
        if (skill == null) return Map.of("success", false, "message", "技能不存在");
        
        // 计算平衡值
        int cost = (int) skill.getOrDefault("cost", 100);
        int level = (int) skill.getOrDefault("level", 1);
        double power = cost * (1 + (level - 1) * 0.5);
        
        return Map.of("success", true, "skill", skill, "power", (int) power);
    }
    
    @GetMapping("/check")
    public Map<String, Object> check(Integer skillId, String quality) {
        Map<String, Object> skill = skillConfig.get(skillId);
        if (skill == null) return Map.of("success", false, "message", "技能不存在");
        
        String need = (String) skill.get("quality");
        Map<String, Integer> q = Map.of("blue", 1, "purple", 2, "orange", 3, "red", 4);
        
        boolean ok = q.getOrDefault(quality, 0) >= q.getOrDefault(need, 0);
        return Map.of("success", true, "canExchange", ok, "need", need, "have", quality);
    }
    
    @GetMapping("/types")
    public Map<String, Object> types() {
        return Map.of("success", true, "types", Arrays.asList(
            Map.of("id", 1, "name", "普通", "quality", "blue"),
            Map.of("id", 2, "name", "稀有", "quality", "purple"),
            Map.of("id", 3, "name", "史诗", "quality", "orange"),
            Map.of("id", 4, "name", "传说", "quality", "red"),
            Map.of("id", 5, "name", "神级", "quality", "red")
        ));
    }
}
