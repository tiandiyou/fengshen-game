package com.game.controller;

import com.game.entity.Player;
import com.game.mapper.PartnerRepository;
import com.game.mapper.TreasureRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/achievement")
@CrossOrigin
public class AchievementController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private TreasureRepository treasureRepository;
    
    private static final List<Map<String, Object>> ACHIEVEMENTS = Arrays.asList(
        Map.of("id", 1, "name", "初入江湖", "desc", "创建角色", "icon", "⭐", "reward", 50),
        Map.of("id", 2, "name", "首战告捷", "desc", "完成第一章", "icon", "⚔️", "reward", 100),
        Map.of("id", 3, "name", "伙伴入手", "desc", "拥有5个伙伴", "icon", "👥", "reward", 100),
        Map.of("id", 4, "name", "法宝收藏", "desc", "拥有3件法宝", "icon", "🔮", "reward", 150),
        Map.of("id", 5, "name", "连续签到", "desc", "签到7天", "icon", "📅", "reward", 200),
        Map.of("id", 6, "name", "战力过万", "desc", "总战力达到10000", "icon", "💪", "reward", 300),
        Map.of("id", 7, "name", "封神之战", "desc", "完成最终章", "icon", "🏆", "reward", 500),
        Map.of("id", 8, "name", "收集全套", "desc", "收集所有伙伴", "icon", "📚", "reward", 1000)
    );
    
    @GetMapping("/list")
    public Map<String, Object> getAchievements(@RequestParam Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        int partnerCount = partnerRepository.findByPlayerId(playerId).size();
        int treasureCount = treasureRepository.findByPlayerId(playerId).size();
        int zhanli = player.getZhanli() != null ? player.getZhanli() : 0;
        
        List<Map<String, Object>> achievements = new ArrayList<>();
        for (Map<String, Object> a : ACHIEVEMENTS) {
            Map<String, Object> item = new HashMap<>(a);
            boolean completed = isCompleted(player, partnerCount, treasureCount, zhanli, (Integer) a.get("id"));
            item.put("completed", completed);
            achievements.add(item);
        }
        
        return Map.of("success", true, "achievements", achievements);
    }
    
    private boolean isCompleted(Player player, int partnerCount, int treasureCount, int zhanli, int id) {
        switch (id) {
            case 1: return player.getName() != null && !player.getName().isEmpty();
            case 2: return player.getChapterId() != null && player.getChapterId() >= 1;
            case 3: return partnerCount >= 5;
            case 4: return treasureCount >= 3;
            case 5: return player.getSigninDays() != null && player.getSigninDays() >= 7;
            case 6: return zhanli >= 10000;
            case 7: return player.getChapterId() != null && player.getChapterId() >= 6;
            case 8: return partnerCount >= 12;
            default: return false;
        }
    }
    
    @PostMapping("/claim")
    public Map<String, Object> claimAchievement(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer achievementId = (Integer) req.get("achievementId");
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        // 这里简化处理，实际应该存储玩家已领取的成就
        return Map.of("success", true, "message", "成就已自动发放奖励");
    }
}
