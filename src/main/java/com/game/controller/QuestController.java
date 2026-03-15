package com.game.controller;

import com.game.config.GameData;
import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/quest")
@CrossOrigin
public class QuestController {
    @Autowired
    private PlayerRepository playerRepository;
    
    private static final List<Map<String, Object>> QUESTS = Arrays.asList(
        Map.of("id", 1, "name", "初战告捷", "desc", "完成第一章", "reward", Map.of("lingqi", 50), "req", Map.of("chapter", 1)),
        Map.of("id", 2, "name", "伙伴招募", "desc", "拥有5个伙伴", "reward", Map.of("lingqi", 100), "req", Map.of("partners", 5)),
        Map.of("id", 3, "name", "战力提升", "desc", "总战力达到1000", "reward", Map.of("lingqi", 150), "req", Map.of("zhanli", 1000)),
        Map.of("id", 4, "name", "连续挑战", "desc", "完成3场战斗", "reward", Map.of("gold", 100), "req", Map.of("battles", 3)),
        Map.of("id", 5, "name", "法宝收集", "desc", "拥有3件法宝", "reward", Map.of("lingqi", 200), "req", Map.of("treasures", 3))
    );
    
    @GetMapping("/list")
    public List<Map<String, Object>> getQuests() {
        return QUESTS;
    }
    
    @PostMapping("/claim")
    public Map<String, Object> claimQuest(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer questId = (Integer) req.get("questId");
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        // 检查任务完成条件
        Map<String, Object> quest = QUESTS.get(questId - 1);
        Map<String, Object> questReq = (Map<String, Object>) quest.get("req");
        
        // 简化检查逻辑
        boolean canClaim = false;
        if (questReq.containsKey("chapter") && player.getChapterId() >= (Integer) questReq.get("chapter")) {
            canClaim = true;
        }
        if (questReq.containsKey("battles") && player.getBattleCount() >= (Integer) questReq.get("battles")) {
            canClaim = true;
        }
        
        if (!canClaim) {
            return Map.of("success", false, "message", "任务未完成");
        }
        
        Map<String, Integer> reward = (Map<String, Integer>) quest.get("reward");
        if (reward.containsKey("lingqi")) {
            player.setLingqi(player.getLingqi() + reward.get("lingqi"));
        }
        if (reward.containsKey("gold")) {
            player.setGold(player.getGold() + reward.get("gold"));
        }
        
        playerRepository.save(player);
        
        return Map.of("success", true, "message", "领取成功", "lingqi", player.getLingqi(), "gold", player.getGold());
    }
}
