package com.game.controller;

import com.game.entity.*;
import com.game.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/season")
@CrossOrigin
public class SeasonController {
    
    @Autowired
    private SeasonRepository seasonRepo;
    
    @Autowired
    private WarCreditRepository creditRepo;
    
    @Autowired
    private EquipmentRepository equipRepo;
    
    // 开启新赛季
    @PostMapping("/start")
    public Map<String, Object> start(Integer days) {
        if (days == null) days = 30;
        
        Season s = new Season();
        s.setSeasonNum(((int)(seasonRepo.count() + 1)));
        s.setStartTime(System.currentTimeMillis());
        s.setEndTime(System.currentTimeMillis() + days * 24L * 3600 * 1000);
        s.setStatus("active");
        seasonRepo.save(s);
        
        return Map.of("success", true, "season", s.getSeasonNum(), "days", days);
    }
    
    // 当前赛季
    @GetMapping("/current")
    public Map<String, Object> current() {
        Optional<Season> s = seasonRepo.findByStatus("active");
        if (!s.isPresent()) {
            return Map.of("success", false, "message", "无活跃赛季");
        }
        return Map.of("success", true, "season", s.get());
    }
    
    // 获取战功
    @GetMapping("/credit")
    public Map<String, Object> credit(Long playerId) {
        Optional<Season> s = seasonRepo.findByStatus("active");
        if (!s.isPresent()) {
            return Map.of("success", false, "message", "无活跃赛季");
        }
        
        Optional<WarCredit> wc = creditRepo.findByPlayerIdAndSeasonId(playerId, s.get().getId());
        if (!wc.isPresent()) {
            WarCredit c = new WarCredit();
            c.setPlayerId(playerId);
            c.setSeasonId(s.get().getId());
            c.setCredit(0);
            c.setKills(0);
            c.setBattles(0);
            c.setUpdateTime(System.currentTimeMillis());
            creditRepo.save(c);
            return Map.of("success", true, "credit", 0, "kills", 0);
        }
        return Map.of("success", true, "credit", wc.get().getCredit(), "kills", wc.get().getKills());
    }
    
    // 增加战功（战斗后调用）
    @PostMapping("/addCredit")
    public Map<String, Object> addCredit(Long playerId, Long allianceId, Integer credit, Integer kills) {
        Optional<Season> s = seasonRepo.findByStatus("active");
        if (!s.isPresent()) {
            return Map.of("success", false, "message", "无活跃赛季");
        }
        
        Optional<WarCredit> wc = creditRepo.findByPlayerIdAndSeasonId(playerId, s.get().getId());
        WarCredit c;
        if (wc.isPresent()) {
            c = wc.get();
            c.setCredit(c.getCredit() + credit);
            c.setKills(c.getKills() + kills);
            c.setBattles(c.getBattles() + 1);
        } else {
            c = new WarCredit();
            c.setPlayerId(playerId);
            c.setAllianceId(allianceId);
            c.setSeasonId(s.get().getId());
            c.setCredit(credit);
            c.setKills(kills);
            c.setBattles(1);
        }
        c.setUpdateTime(System.currentTimeMillis());
        creditRepo.save(c);
        
        return Map.of("success", true, "credit", c.getCredit(), "kills", c.getKills());
    }
    
    // 封神榜排行
    @GetMapping("/ranking")
    public Map<String, Object> ranking() {
        Optional<Season> s = seasonRepo.findByStatus("active");
        if (!s.isPresent()) {
            return Map.of("success", false, "message", "无活跃赛季");
        }
        
        List<WarCredit> credits = creditRepo.findAll();
        credits.sort((a, b) -> b.getCredit() - a.getCredit());
        
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (int i = 0; i < Math.min(10, credits.size()); i++) {
            WarCredit c = credits.get(i);
            Map<String, Object> m = new HashMap<>();
            m.put("rank", i + 1);
            m.put("playerId", c.getPlayerId());
            m.put("credit", c.getCredit());
            m.put("kills", c.getKills());
            ranking.add(m);
        }
        
        return Map.of("success", true, "season", s.get().getSeasonNum(), "ranking", ranking);
    }
    
    // 兑换装备
    @PostMapping("/exchange")
    public Map<String, Object> exchange(Long playerId, String type, String name, String special, Integer cost) {
        Optional<Season> s = seasonRepo.findByStatus("active");
        if (s.isPresent()) {
            return Map.of("success", false, "message", "赛季进行中不可兑换");
        }
        
        Optional<WarCredit> wc = creditRepo.findByPlayerIdAndSeasonId(playerId, s.get().getId());
        if (!wc.isPresent() || wc.get().getCredit() < cost) {
            return Map.of("success", false, "message", "战功不足");
        }
        
        WarCredit c = wc.get();
        c.setCredit(c.getCredit() - cost);
        creditRepo.save(c);
        
        Equipment e = new Equipment();
        e.setPlayerId(playerId);
        e.setType(type);
        e.setName(name);
        e.setSpecial(special);
        e.setLevel(1);
        e.setQuality(1);
        equipRepo.save(e);
        
        return Map.of("success", true, "equipment", e, "remainingCredit", c.getCredit());
    }
    
    // 我的装备
    @GetMapping("/equipment")
    public Map<String, Object> equipment(Long playerId) {
        List<Equipment> equips = equipRepo.findByPlayerId(playerId);
        return Map.of("success", true, "equipment", equips);
    }
}
