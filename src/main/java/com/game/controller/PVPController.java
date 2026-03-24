package com.game.controller;

import com.game.service.PVPMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pvp")
@CrossOrigin
public class PVPController {

    @Autowired
    private PVPMatchService pvpMatchService;

    /**
     * 发起PVP匹配
     */
    @PostMapping("/match")
    public Map<String, Object> startMatch(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long cityId = ((Number) req.get("cityId")).longValue();
        
        return pvpMatchService.startMatch(playerId, cityId);
    }

    /**
     * 查询匹配状态
     */
    @GetMapping("/status")
    public Map<String, Object> getMatchStatus(@RequestParam Long playerId) {
        return pvpMatchService.findMatch(playerId);
    }

    /**
     * 取消匹配
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancelMatch(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        return pvpMatchService.cancelMatch(playerId);
    }

    /**
     * 获取排行榜
     */
    @GetMapping("/ranking")
    public Map<String, Object> getRanking(@RequestParam(defaultValue = "20") int limit) {
        return pvpMatchService.getRanking(limit);
    }

    /**
     * 获取战斗历史
     */
    @GetMapping("/history")
    public Map<String, Object> getBattleHistory(
            @RequestParam Long playerId,
            @RequestParam(defaultValue = "10") int limit) {
        return pvpMatchService.getBattleHistory(playerId, limit);
    }
}
