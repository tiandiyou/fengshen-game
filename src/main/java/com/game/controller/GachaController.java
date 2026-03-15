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
@RequestMapping("/api/gacha")
@CrossOrigin
public class GachaController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    
    @PostMapping("/draw")
    public Map<String, Object> draw(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer cost = (Integer) req.getOrDefault("cost", 50);
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        if (player.getLingqi() < cost) {
            return Map.of("success", false, "message", "灵气不足");
        }
        
        player.setLingqi(player.getLingqi() - cost);
        
        // 抽卡概率
        double r = Math.random();
        String quality;
        if (r < 0.03) quality = "red";
        else if (r < 0.15) quality = "orange";
        else if (r < 0.40) quality = "purple";
        else quality = "blue";
        
        // 筛选伙伴
        List<Map<String, Object>> candidates = new ArrayList<>();
        for (Map<String, Object> p : GameData.PARTNERS) {
            String q = (String) p.get("quality");
            if ("red".equals(quality)) {
                if ("red".equals(q)) candidates.add(p);
            } else if ("orange".equals(quality)) {
                if ("orange".equals(q) || "red".equals(q)) candidates.add(p);
            } else if ("purple".equals(quality)) {
                candidates.add(p);
            } else {
                candidates.add(p);
            }
        }
        
        Map<String, Object> pdata = candidates.get((int) (Math.random() * candidates.size()));
        
        // 创建伙伴
        Partner partner = new Partner();
        partner.setPlayerId(playerId);
        partner.setPartnerId((Integer) pdata.get("id"));
        partner.setName((String) pdata.get("name"));
        partner.setIcon((String) pdata.get("icon"));
        partner.setQuality((String) pdata.get("quality"));
        partner.setHp((Integer) pdata.get("hp"));
        partner.setAtk((Integer) pdata.get("atk"));
        partner.setSpeed((Integer) pdata.get("speed"));
        partner.setLevel(1);
        partner.setStar(0);
        partner.setSkills(String.join(",", (List<String>) pdata.get("skills")));
        
        partnerRepository.save(partner);
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("partner", pdata);
        result.put("quality", quality);
        return result;
    }
    
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        int cost = (int) (50 * Math.pow(1.5, partner.getLevel()));
        if (player.getLingqi() < cost) {
            return Map.of("success", false, "message", "灵气不足");
        }
        
        player.setLingqi(player.getLingqi() - cost);
        partner.setLevel(partner.getLevel() + 1);
        partner.setHp((int) (partner.getHp() * 1.1));
        partner.setAtk((int) (partner.getAtk() * 1.1));
        
        playerRepository.save(player);
        partnerRepository.save(partner);
        
        return Map.of("success", true, "level", partner.getLevel());
    }
}
