package com.game.controller;

import com.game.config.GameData;
import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.entity.Treasure;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import com.game.mapper.TreasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/player")
@CrossOrigin
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private TreasureRepository treasureRepository;
    
    @GetMapping("/{id}")
    public Map<String, Object> getPlayer(@PathVariable Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        List<Partner> partners = partnerRepository.findByPlayerId(id);
        List<Treasure> treasures = treasureRepository.findByPlayerId(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("player", player);
        result.put("partners", partners);
        result.put("treasures", treasures);
        return result;
    }
    
    @PostMapping("/create")
    public Map<String, Object> createPlayer(@RequestBody Map<String, Object> req) {
        Player player = new Player();
        player.setName((String) req.getOrDefault("name", "阐教弟子"));
        player.setLingqi(200);
        player.setGold(100);
        
        // 赠送哪吒
        Map<String, Object> pdata = GameData.PARTNERS.get(0);
        Partner p = new Partner();
        p.setPlayerId(player.getId());
        p.setPartnerId((Integer) pdata.get("id"));
        p.setName((String) pdata.get("name"));
        p.setIcon((String) pdata.get("icon"));
        p.setQuality((String) pdata.get("quality"));
        p.setHp((Integer) pdata.get("hp"));
        p.setAtk((Integer) pdata.get("atk"));
        p.setSpeed((Integer) pdata.get("speed"));
        p.setLevel(1);
        p.setStar(0);
        p.setSkills(String.join(",", (List<String>) pdata.get("skills")));
        
        player = playerRepository.save(player);
        p.setPlayerId(player.getId());
        partnerRepository.save(p);
        
        return Map.of("success", true, "playerId", player.getId(), "message", "创建成功");
    }
    
    @PostMapping("/update")
    public Map<String, Object> updatePlayer(@RequestBody Player player) {
        playerRepository.save(player);
        return Map.of("success", true);
    }
    
    @GetMapping("/chapters")
    public List<Map<String, Object>> getChapters() {
        return GameData.CHAPTERS;
    }
    
    @GetMapping("/treasures")
    public List<Map<String, Object>> getTreasures() {
        return GameData.TREASURES;
    }
}
