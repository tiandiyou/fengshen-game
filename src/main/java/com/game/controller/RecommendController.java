package com.game.controller;

import com.game.entity.Partner;
import com.game.mapper.PartnerRepository;
import com.game.service.TeamRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class RecommendController {
    
    @Autowired
    private PartnerRepository partnerRepo;
    
    @Autowired
    private TeamRecommendService recommendSvc;
    
    @GetMapping("/recommend")
    public Map<String, Object> recommend(Long playerId) {
        List<Partner> partners = partnerRepo.findByPlayerId(playerId);
        if (partners.size() < 3) {
            return Map.of("success", false, "message", "伙伴不足3人");
        }
        List<Partner> enemies = partners.subList(0, 3);
        return recommendSvc.recommend(enemies, partners);
    }
    
    @GetMapping("/best")
    public Map<String, Object> best(Long playerId) {
        List<Partner> partners = partnerRepo.findByPlayerId(playerId);
        if (partners.size() < 3) {
            return Map.of("success", false, "message", "伙伴不足3人");
        }
        partners.sort((a, b) -> power(b) - power(a));
        List<Partner> team = partners.subList(0, 3);
        int totalPower = team.stream().mapToInt(this::power).sum();
        return Map.of("success", true, "team", team, "power", totalPower);
    }
    
    private int power(Partner p) {
        return p.getAtk() * 2 + p.getHp() / 10;
    }
}
