package com.game.controller.admin;

import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {
    @Autowired
    private PlayerRepository playerRepository;
    
    // 管理员密钥验证（简单版）
    private static final String ADMIN_KEY = "admin123";
    
    private boolean verifyAdmin(String key) {
        return ADMIN_KEY.equals(key);
    }
    
    // 获取所有玩家
    @GetMapping("/players")
    public Map<String, Object> getAllPlayers(@RequestHeader("X-Admin-Key") String key) {
        if (!verifyAdmin(key)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        List<Player> players = playerRepository.findAll();
        return Map.of("success", true, "players", players, "total", players.size());
    }
    
    // 获取玩家详情
    @GetMapping("/player/{id}")
    public Map<String, Object> getPlayerDetail(@RequestHeader("X-Admin-Key") String key, @PathVariable Long id) {
        if (!verifyAdmin(key)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        Player player = playerRepository.findById(id).orElse(null);
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        return Map.of("success", true, "player", player);
    }
    
    // 修改玩家资源
    @PostMapping("/player/modify")
    public Map<String, Object> modifyPlayer(@RequestHeader("X-Admin-Key") String key, @RequestBody Map<String, Object> req) {
        if (!verifyAdmin(key)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        Long playerId = ((Number) req.get("playerId")).longValue();
        Player player = playerRepository.findById(playerId).orElse(null);
        
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        
        if (req.containsKey("lingqi")) {
            player.setLingqi((Integer) req.get("lingqi"));
        }
        if (req.containsKey("gold")) {
            player.setGold((Integer) req.get("gold"));
        }
        if (req.containsKey("name")) {
            player.setName((String) req.get("name"));
        }
        
        playerRepository.save(player);
        
        return Map.of("success", true, "message", "修改成功");
    }
    
    // 发送邮件/站内信
    @PostMapping("/mail/send")
    public Map<String, Object> sendMail(@RequestHeader("X-Admin-Key") String key, @RequestBody Map<String, Object> req) {
        if (!verifyAdmin(key)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        // 简化版：直接增加资源
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer lingqi = (Integer) req.getOrDefault("lingqi", 0);
        Integer gold = (Integer) req.getOrDefault("gold", 0);
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        
        player.setLingqi(player.getLingqi() + lingqi);
        player.setGold(player.getGold() + gold);
        playerRepository.save(player);
        
        return Map.of("success", true, "message", "发送成功");
    }
    
    // 统计数据
    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestHeader("X-Admin-Key") String key) {
        if (!verifyAdmin(key)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        List<Player> players = playerRepository.findAll();
        
        int totalPlayers = players.size();
        int totalLingqi = players.stream().mapToInt(p -> p.getLingqi() != null ? p.getLingqi() : 0).sum();
        int totalGold = players.stream().mapToInt(p -> p.getGold() != null ? p.getGold() : 0).sum();
        int totalZhanli = players.stream().mapToInt(p -> p.getZhanli() != null ? p.getZhanli() : 0).sum();
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("totalPlayers", totalPlayers);
        result.put("totalLingqi", totalLingqi);
        result.put("totalGold", totalGold);
        result.put("avgZhanli", totalPlayers > 0 ? totalZhanli / totalPlayers : 0);
        
        return result;
    }
}
