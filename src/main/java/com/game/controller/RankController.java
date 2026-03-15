package com.game.controller;

import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rank")
@CrossOrigin
public class RankController {
    @Autowired
    private PlayerRepository playerRepository;
    
    @GetMapping("/list")
    public Map<String, Object> getRanks() {
        List<Player> allPlayers = playerRepository.findAll();
        
        // 战力榜
        List<Player> zhanliRank = allPlayers.stream()
            .sorted((a, b) -> b.getZhanli() - a.getZhanli())
            .limit(10)
            .collect(Collectors.toList());
        
        // 章节榜
        List<Player> chapterRank = allPlayers.stream()
            .sorted((a, b) -> b.getChapterId() - a.getChapterId())
            .limit(10)
            .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("zhanliRank", zhanliRank);
        result.put("chapterRank", chapterRank);
        return result;
    }
}
