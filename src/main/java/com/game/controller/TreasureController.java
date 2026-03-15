package com.game.controller;

import com.game.config.GameData;
import com.game.entity.Treasure;
import com.game.entity.Player;
import com.game.mapper.TreasureRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/treasure")
@CrossOrigin
public class TreasureController {
    @Autowired
    private TreasureRepository treasureRepository;
    @Autowired
    private PlayerRepository playerRepository;
    
    // 获取法宝列表
    @GetMapping("/list")
    public Map<String, Object> getTreasures(@RequestParam Long playerId) {
        List<Treasure> treasures = treasureRepository.findByPlayerId(playerId);
        return Map.of("success", true, "treasures", treasures, "allTreasures", GameData.TREASURES);
    }
    
    // 领取法宝
    @PostMapping("/receive")
    public Map<String, Object> receiveTreasure(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer treasureId = (Integer) req.get("treasureId");
        
        // 检查是否已拥有
        List<Treasure> existing = treasureRepository.findByPlayerId(playerId);
        boolean has = existing.stream().anyMatch(t -> t.getTreasureId().equals(treasureId));
        if (has) {
            return Map.of("success", false, "message", "已有此法宝");
        }
        
        // 获取法宝数据
        Map<String, Object> tData = GameData.TREASURES.get(treasureId - 1);
        
        Treasure treasure = new Treasure();
        treasure.setPlayerId(playerId);
        treasure.setTreasureId(treasureId);
        treasure.setName((String) tData.get("name"));
        treasure.setIcon((String) tData.get("icon"));
        treasure.setBonus((String) tData.get("bonus"));
        
        treasureRepository.save(treasure);
        
        return Map.of("success", true, "treasure", tData, "message", "获得" + tData.get("name"));
    }
}
