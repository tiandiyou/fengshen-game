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
     * 队伍对战模拟 (从数据库读取)
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
        
        // 获取阵型ID
        Long formationId1 = req.get("formationId1") != null ? ((Number) req.get("formationId1")).longValue() : null;
        Long formationId2 = req.get("formationId2") != null ? ((Number) req.get("formationId2")).longValue() : null;
        
        int power1 = warBattleService.calcTeamPower(team1);
        int power2 = warBattleService.calcTeamPower(team2);
        
        Map<String, Object> battle = warBattleService.battle(team1, team2, formationId1, formationId2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("team1Power", power1);
        result.put("team2Power", power2);
        result.put("battle", battle);
        
        return result;
    }
    
    /**
     * 队伍对战模拟 (直接传伙伴数据，用于测试)
     */
    @PostMapping("/simulate/test")
    public Map<String, Object> simulateTest(@RequestBody Map<String, Object> req) {
        // 直接从请求中解析伙伴数据
        List<Partner> team1 = parsePartnerData(req.get("team1"));
        List<Partner> team2 = parsePartnerData(req.get("team2"));
        
        if (team1.isEmpty() || team2.isEmpty()) {
            return Map.of("success", false, "message", "队伍人数不足");
        }
        
        // 阵型加成测试
        String formation1 = (String) req.getOrDefault("formation1", "锋矢阵");
        String formation2 = (String) req.getOrDefault("formation2", "八卦阵");
        
        // 创建模拟的阵型加成
        Map<String, Double> bonus1 = com.game.service.FormationService.FORMATION_BONUS.getOrDefault(formation1, Map.of());
        Map<String, Double> bonus2 = com.game.service.FormationService.FORMATION_BONUS.getOrDefault(formation2, Map.of());
        
        int power1 = warBattleService.calcTeamPower(team1, bonus1);
        int power2 = warBattleService.calcTeamPower(team2, bonus2);
        
        Map<String, Object> battle = warBattleService.battle(team1, team2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("team1Power", power1);
        result.put("team2Power", team2);
        result.put("team2Power", power2);
        result.put("team1Formation", formation1);
        result.put("team2Formation", formation2);
        result.put("team1Bonus", bonus1);
        result.put("team2Bonus", bonus2);
        result.put("battle", battle);
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private List<Partner> parsePartnerData(Object obj) {
        List<Partner> partners = new ArrayList<>();
        if (obj instanceof List) {
            for (Object o : (List<?>) obj) {
                if (o instanceof Map) {
                    Map<String, Object> m = (Map<String, Object>) o;
                    Partner p = new Partner();
                    p.setId(m.get("id") != null ? ((Number) m.get("id")).longValue() : 0L);
                    p.setName((String) m.getOrDefault("name", "未知"));
                    p.setAtk(m.get("atk") != null ? ((Number) m.get("atk")).intValue() : 1000);
                    p.setHp(m.get("hp") != null ? ((Number) m.get("hp")).intValue() : 5000);
                    p.setSpeed(m.get("speed") != null ? ((Number) m.get("speed")).intValue() : 100);
                    p.setType((String) m.getOrDefault("type", "步"));
                    p.setLevel(m.get("level") != null ? ((Number) m.get("level")).intValue() : 1);
                    partners.add(p);
                }
            }
        }
        return partners;
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
