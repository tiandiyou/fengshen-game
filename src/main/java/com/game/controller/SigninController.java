package com.game.controller;

import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/signin")
@CrossOrigin
public class SigninController {
    @Autowired
    private PlayerRepository playerRepository;
    
    private static final int[] REWARDS = {50, 80, 100, 150, 200, 300, 500};
    
    @GetMapping("/status")
    public Map<String, Object> getSigninStatus(@RequestParam Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        boolean canSign = !today.equals(player.getSigninLastDate());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("days", player.getSigninDays());
        result.put("canSign", canSign);
        result.put("today", today);
        result.put("rewards", REWARDS);
        return result;
    }
    
    @PostMapping("/do")
    public Map<String, Object> doSignin(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        if (today.equals(player.getSigninLastDate())) {
            return Map.of("success", false, "message", "今日已签到");
        }
        
        player.setSigninDays(player.getSigninDays() + 1);
        player.setSigninLastDate(today);
        
        int reward = REWARDS[(player.getSigninDays() - 1) % 7];
        player.setLingqi(player.getLingqi() + reward);
        
        playerRepository.save(player);
        
        return Map.of("success", true, "message", "签到成功", "reward", reward, "lingqi", player.getLingqi());
    }
}
