package com.game.controller;

import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/partner")
@CrossOrigin
public class PartnerController {
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    
    // 获取伙伴列表
    @GetMapping("/list")
    public Map<String, Object> getPartners(@RequestParam Long playerId) {
        List<Partner> partners = partnerRepository.findByPlayerId(playerId);
        return Map.of("success", true, "partners", partners);
    }
    
    // 选择/取消伙伴
    @PostMapping("/select")
    public Map<String, Object> selectPartner(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        Boolean selected = (Boolean) req.get("selected");
        
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        if (partner == null || !partner.getPlayerId().equals(playerId)) {
            return Map.of("success", false, "message", "伙伴不存在");
        }
        
        partner.setSelected(selected);
        partnerRepository.save(partner);
        
        // 检查已选数量
        List<Partner> all = partnerRepository.findByPlayerId(playerId);
        long selectedCount = all.stream().filter(p -> p.getSelected() != null && p.getSelected()).count();
        
        return Map.of("success", true, "selectedCount", selectedCount);
    }
    
    // 升星
    @PostMapping("/star")
    public Map<String, Object> starPartner(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        if (partner.getStar() >= 5) {
            return Map.of("success", false, "message", "最高5星");
        }
        
        int cost = (int) (200 * Math.pow(2, partner.getStar()));
        if (player.getLingqi() < cost) {
            return Map.of("success", false, "message", "灵气不足，需要" + cost);
        }
        
        player.setLingqi(player.getLingqi() - cost);
        partner.setStar(partner.getStar() + 1);
        partner.setHp((int) (partner.getHp() * 1.3));
        partner.setAtk((int) (partner.getAtk() * 1.3));
        
        playerRepository.save(player);
        partnerRepository.save(partner);
        
        return Map.of("success", true, "star", partner.getStar(), "lingqi", player.getLingqi());
    }
    
    // 删除伙伴
    @PostMapping("/delete")
    public Map<String, Object> deletePartner(@RequestBody Map<String, Object> req) {
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        
        partnerRepository.deleteById(partnerId);
        return Map.of("success", true, "message", "删除成功");
    }
}
