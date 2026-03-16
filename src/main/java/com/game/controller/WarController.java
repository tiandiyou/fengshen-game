package com.game.controller;

import com.game.entity.City;
import com.game.entity.Partner;
import com.game.entity.Team;
import com.game.mapper.CityRepository;
import com.game.mapper.PartnerRepository;
import com.game.mapper.TeamRepository;
import com.game.service.WarBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/war")
@CrossOrigin
public class WarController {
    
    @Autowired
    private WarBattleService warBattleService;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private PartnerRepository partnerRepository;
    
    /**
     * 攻城战
     */
    @PostMapping("/siege")
    public Map<String, Object> siege(@RequestBody Map<String, Object> req) {
        Long cityId = ((Number) req.get("cityId")).longValue();
        Long attackerId = ((Number) req.get("attackerId")).longValue();
        
        // 获取城池
        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            return Map.of("success", false, "message", "城池不存在");
        }
        
        // 获取攻击方队伍
        List<Partner> attackTeam = getTeamPartners(attackerId, cityId);
        if (attackTeam.isEmpty()) {
            return Map.of("success", false, "message", "请先设置进攻队伍");
        }
        
        // 获取防守方队伍
        Long defenderId = city.getPlayerId();
        List<Partner> defendTeam = getTeamPartners(defenderId, cityId);
        
        // 如果没有防守队伍，用城防兵力
        if (defendTeam.isEmpty()) {
            defendTeam = attackTeam; // 临时用相同队伍，实际应用城防NPC
        }
        
        // 执行战斗
        Map<String, Object> result = warBattleService.siegeWar(cityId, attackerId, defenderId, attackTeam, defendTeam);
        result.put("success", true);
        
        return result;
    }
    
    /**
     * 队伍对战模拟
     */
    @PostMapping("/simulate")
    public Map<String, Object> simulate(@RequestBody Map<String, Object> req) {
        // 队伍1
        List<Long> team1Ids = parsePartnerIds(req.get("team1"));
        List<Partner> team1 = new ArrayList<>();
        for (Long id : team1Ids) {
            partnerRepository.findById(id).ifPresent(team1::add);
        }
        
        // 队伍2
        List<Long> team2Ids = parsePartnerIds(req.get("team2"));
        List<Partner> team2 = new ArrayList<>();
        for (Long id : team2Ids) {
            partnerRepository.findById(id).ifPresent(team2::add);
        }
        
        if (team1.isEmpty() || team2.isEmpty()) {
            return Map.of("success", false, "message", "队伍人数不足");
        }
        
        int power1 = warBattleService.calcTeamPower(team1);
        int power2 = warBattleService.calcTeamPower(team2);
        
        Map<String, Object> battle = warBattleService.battle(team1, team2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("team1Power", power1);
        result.put("team2Power", power2);
        result.put("battle", battle);
        
        return result;
    }
    
    /**
     * 获取队伍战力
     */
    @GetMapping("/team-power")
    public Map<String, Object> getTeamPower(@RequestParam Long playerId, @RequestParam Long cityId) {
        List<Partner> team = getTeamPartners(playerId, cityId);
        
        if (team.isEmpty()) {
            return Map.of("success", false, "message", "没有队伍");
        }
        
        int power = warBattleService.calcTeamPower(team);
        
        // 兵种统计
        Map<String, Integer> units = new HashMap<>();
        for (Partner p : team) {
            String u = p.getType() != null ? p.getType() : "步";
            units.put(u, units.getOrDefault(u, 0) + 1);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("power", power);
        result.put("teamSize", team.size());
        result.put("units", units);
        
        return result;
    }
    
    /**
     * 获取玩家队伍伙伴
     */
    private List<Partner> getTeamPartners(Long playerId, Long cityId) {
        List<Partner> partners = new ArrayList<>();
        
        // 查找队伍
        List<Team> teams = teamRepository.findByPlayerIdAndCityId(playerId, cityId);
        if (!teams.isEmpty()) {
            Team team = teams.get(0);
            
            if (team.getPartner1Id() != null) {
                partnerRepository.findById(team.getPartner1Id()).ifPresent(partners::add);
            }
            if (team.getPartner2Id() != null) {
                partnerRepository.findById(team.getPartner2Id()).ifPresent(partners::add);
            }
            if (team.getPartner3Id() != null) {
                partnerRepository.findById(team.getPartner3Id()).ifPresent(partners::add);
            }
        }
        
        return partners;
    }
    
    @SuppressWarnings("unchecked")
    private List<Long> parsePartnerIds(Object obj) {
        List<Long> ids = new ArrayList<>();
        if (obj instanceof List) {
            for (Object o : (List<?>) obj) {
                if (o instanceof Number) {
                    ids.add(((Number) o).longValue());
                }
            }
        }
        return ids;
    }
}
