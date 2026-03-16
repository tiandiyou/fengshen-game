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
        Optional<Player> opt = playerRepository.findById(id);
        if (!opt.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "玩家不存在");
            return result;
        }
        
        Player player = opt.get();
        List<Partner> partners = partnerRepository.findByPlayerId(id);
        List<Treasure> treasures = treasureRepository.findByPlayerId(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("player", player);
        result.put("partners", partners);
        result.put("treasures", treasures);
        return result;
    }
    
    // 注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> req) {
        String username = (String) req.get("username");
        String password = (String) req.get("password");
        String name = (String) req.getOrDefault("name", username);
        
        if (username == null || password == null) {
            return Map.of("success", false, "message", "请输入账号密码");
        }
        
        // 检查是否已存在
        List<Player> existing = playerRepository.findAll();
        for(Player p : existing) {
            if (p.getName() != null && p.getName().equals(username)) {
                return Map.of("success", false, "message", "账号已存在");
            }
        }
        
        Player p = new Player();
        p.setName(username);
        p.setGold(1000);
        p.setLingqi(100);
        p.setLevel(1);
        p.setExp(0);
        playerRepository.save(p);
        
        return Map.of("success", true, "playerId", p.getId(), "name", name);
    }
    
    // 登录
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> req) {
        String username = (String) req.get("username");
        String password = (String) req.get("password");
        
        if (username == null || password == null) {
            return Map.of("success", false, "message", "请输入账号密码");
        }
        
        List<Player> all = playerRepository.findAll();
        for(Player p : all) {
            if (p.getName() != null && p.getName().equals(username)) {
                return Map.of("success", true, "playerId", p.getId(), "name", p.getName());
            }
        }
        
        return Map.of("success", false, "message", "账号不存在");
    }
    
    @PostMapping("/create")
    public Map<String, Object> createPlayer(@RequestBody Map<String, Object> req) {
        Player player = new Player();
        player.setName((String) req.getOrDefault("name", "阐教弟子"));
        player.setLingqi(200);
        player.setGold(100);
        player.setLevel(1);
        player.setExp(0);
        
        // 赠送哪吒
        Map<String, Object> pdata = GameData.PARTNERS.get(0);
        Partner p = new Partner();
        p.setPlayerId(null);
        p.setPartnerId((Integer) pdata.get("id"));
        p.setName((String) pdata.get("name"));
        p.setIcon((String) pdata.get("icon"));
        p.setQuality((String) pdata.get("quality"));
        p.setAtk((Integer) pdata.get("atk"));
        p.setIntelligence(75);
        p.setLead(80);
        p.setSpeed((Integer) pdata.get("speed"));
        p.setLevel(1);
        p.setStar(1);
        p.setMaxLevel(80);
        p.setMaxTroops(10000);
        p.setGrowthAtk(2);
        p.setGrowthInt(2);
        p.setGrowthLead(1);
        p.setGrowthSpeed(1);
        
        player = playerRepository.save(player);
        
        p.setPlayerId(player.getId());
        partnerRepository.save(p);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("playerId", player.getId());
        result.put("message", "创建成功");
        return result;
    }
    
    @PostMapping("/update")
    public Map<String, Object> updatePlayer(@RequestBody Player player) {
        playerRepository.save(player);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
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
