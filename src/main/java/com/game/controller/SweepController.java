package com.game.controller;

import com.game.config.GameData;
import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/sweep")
@CrossOrigin
public class SweepController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    
    // 检查是否可以扫荡
    @GetMapping("/check")
    public Map<String, Object> checkSweep(@RequestParam Long playerId, @RequestParam Integer chapterId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        // 只能扫荡已通关的章节
        if (player.getChapterId() == null || player.getChapterId() < chapterId) {
            return Map.of("success", false, "message", "未通关该章节");
        }
        
        return Map.of("success", true, "canSweep", true);
    }
    
    // 执行扫荡
    @PostMapping("/run")
    public Map<String, Object> runSweep(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer chapterId = (Integer) req.get("chapterId");
        Integer times = (Integer) req.getOrDefault("times", 1);
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        if (player.getChapterId() < chapterId) {
            return Map.of("success", false, "message", "未通关该章节");
        }
        
        // 获取章节奖励
        Map<String, Object> chapter = GameData.CHAPTERS.get(chapterId - 1);
        Map<String, Integer> reward = (Map<String, Integer>) chapter.get("reward");
        
        int lingqiReward = reward.get("lingqi") * times;
        int goldReward = reward.get("gold") * times;
        
        player.setLingqi(player.getLingqi() + lingqiReward);
        player.setGold(player.getGold() + goldReward);
        player.setBattleCount(player.getBattleCount() + times);
        
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("lingqi", lingqiReward);
        result.put("gold", goldReward);
        result.put("times", times);
        result.put("totalLingqi", player.getLingqi());
        result.put("totalGold", player.getGold());
        
        return result;
    }
}
