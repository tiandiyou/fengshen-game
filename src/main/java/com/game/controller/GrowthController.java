package com.game.controller;

import com.game.service.GrowthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/growth")
@CrossOrigin
public class GrowthController {
    
    @Autowired
    private GrowthService growthService;
    
    /**
     * 升级
     */
    @PostMapping("/level-up")
    public Map<String, Object> levelUp(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        return growthService.levelUp(playerId, partnerId);
    }
    
    /**
     * 升星
     */
    @PostMapping("/star-up")
    public Map<String, Object> starUp(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        Long consumePartnerId = ((Number) req.get("consumePartnerId")).longValue();
        return growthService.starUp(playerId, partnerId, consumePartnerId);
    }
    
    /**
     * 突破
     */
    @PostMapping("/breakthrough")
    public Map<String, Object> breakthrough(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        return growthService.breakthrough(playerId, partnerId);
    }
    
    /**
     * 获取成长信息
     */
    @GetMapping("/info")
    public Map<String, Object> getGrowthInfo(@RequestParam Long partnerId) {
        return growthService.getGrowthInfo(partnerId);
    }
}
