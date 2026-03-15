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
@RequestMapping("/api/battle")
@CrossOrigin
public class BattleController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    
    @PostMapping("/start")
    public Map<String, Object> startBattle(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer chapterId = (Integer) req.get("chapterId");
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        List<Partner> partners = partnerRepository.findByPlayerId(playerId);
        List<Partner> selected = new ArrayList<>();
        for (Partner p : partners) {
            if (p.getSelected() != null && p.getSelected()) {
                selected.add(p);
            }
        }
        
        if (selected.isEmpty()) {
            return Map.of("success", false, "message", "请先选择上阵伙伴");
        }
        
        Map<String, Object> chapter = GameData.CHAPTERS.get(chapterId - 1);
        List<Map<String, Object>> enemies = (List<Map<String, Object>>) chapter.get("enemies");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("playerUnits", selected);
        result.put("enemyUnits", enemies);
        result.put("chapterId", chapterId);
        return result;
    }
    
    @PostMapping("/result")
    public Map<String, Object> battleResult(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer chapterId = (Integer) req.get("chapterId");
        Boolean win = (Boolean) req.get("win");
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        if (win) {
            Map<String, Object> chapter = GameData.CHAPTERS.get(chapterId - 1);
            Map<String, Integer> reward = (Map<String, Integer>) chapter.get("reward");
            player.setLingqi(player.getLingqi() + reward.get("lingqi"));
            player.setGold(player.getGold() + reward.get("gold"));
            player.setBattleCount(player.getBattleCount() + 1);
            if (chapterId > player.getChapterId()) {
                player.setChapterId(chapterId);
            }
        }
        
        // 计算战力
        List<Partner> partners = partnerRepository.findByPlayerId(playerId);
        int zhanli = 0;
        for (Partner p : partners) {
            zhanli += p.getHp() + p.getAtk() * 2;
        }
        player.setZhanli(zhanli + player.getName().length() * 10);
        playerRepository.save(player);
        
        return Map.of("success", true, "lingqi", player.getLingqi(), "gold", player.getGold());
    }
}
