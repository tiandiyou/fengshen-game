package com.game.controller;

import com.game.entity.City;
import com.game.entity.Partner;
import com.game.entity.Team;
import com.game.mapper.CityRepository;
import com.game.mapper.PartnerRepository;
import com.game.mapper.TeamRepository;
import com.game.service.FactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/team")
@CrossOrigin
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private FactionService factionService;
    
    // 每队最大伙伴数
    private static final int MAX_PARTNERS_PER_TEAM = 3;
    
    // 军营等级对应最大队伍数
    private static final int[] MAX_TEAMS_BY_BARRACKS_LEVEL = {0, 1, 2, 3, 4, 5};
    
    /**
     * 获取玩家所有队伍
     */
    @GetMapping("/list")
    public Map<String, Object> listTeams(@RequestParam Long playerId, @RequestParam(required = false) Long cityId) {
        List<Team> teams;
        if (cityId != null) {
            teams = teamRepository.findByPlayerIdAndCityId(playerId, cityId);
        } else {
            teams = teamRepository.findByPlayerId(playerId);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Team t : teams) {
            result.add(buildTeamInfo(t));
        }
        
        return Map.of("success", true, "teams", result);
    }
    
    /**
     * 创建/设置队伍
     */
    @PostMapping("/set")
    public Map<String, Object> setTeam(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long cityId = ((Number) req.get("cityId")).longValue();
        Integer teamIndex = (Integer) req.get("teamIndex");
        List<Long> partnerIds = new ArrayList<>();
        
        // 解析伙伴ID列表
        Object partnerIdsObj = req.get("partnerIds");
        if (partnerIdsObj instanceof List) {
            for (Object o : (List<?>) partnerIdsObj) {
                if (o instanceof Number) {
                    partnerIds.add(((Number) o).longValue());
                }
            }
        }
        
        // 验证城池
        Optional<City> cityOpt = cityRepository.findById(cityId);
        if (!cityOpt.isPresent()) {
            return Map.of("success", false, "message", "城池不存在");
        }
        City city = cityOpt.get();
        
        // 检查队伍数限制
        int maxTeams = getMaxTeams(city.getBarracksLevel());
        if (teamIndex >= maxTeams) {
            return Map.of("success", false, "message", "军营等级不足，最多只能设置" + maxTeams + "个队伍");
        }
        
        // 检查每队伙伴数限制
        if (partnerIds.size() > MAX_PARTNERS_PER_TEAM) {
            return Map.of("success", false, "message", "每队最多" + MAX_PARTNERS_PER_TEAM + "个伙伴");
        }
        
        // 验证伙伴归属
        for (Long pid : partnerIds) {
            Optional<Partner> pOpt = partnerRepository.findById(pid);
            if (!pOpt.isPresent() || !pOpt.get().getPlayerId().equals(playerId)) {
                return Map.of("success", false, "message", "伙伴ID " + pid + " 不存在或不归属该玩家");
            }
        }
        
        // 查找或创建队伍
        Optional<Team> existingOpt = teamRepository.findByPlayerIdAndCityIdAndTeamIndex(playerId, cityId, teamIndex);
        Team team;
        if (existingOpt.isPresent()) {
            team = existingOpt.get();
        } else {
            team = new Team();
            team.setPlayerId(playerId);
            team.setCityId(cityId);
            team.setTeamIndex(teamIndex);
            team.setCreateTime(System.currentTimeMillis());
        }
        
        // 设置伙伴
        team.setPartner1Id(partnerIds.size() > 0 ? partnerIds.get(0) : null);
        team.setPartner2Id(partnerIds.size() > 1 ? partnerIds.get(1) : null);
        team.setPartner3Id(partnerIds.size() > 2 ? partnerIds.get(2) : null);
        team.setUpdateTime(System.currentTimeMillis());
        
        // 设置默认名称
        if (team.getName() == null || team.getName().isEmpty()) {
            team.setName("队伍" + (teamIndex + 1));
        }
        
        teamRepository.save(team);
        
        return Map.of("success", true, "message", "队伍设置成功", "team", buildTeamInfo(team));
    }
    
    /**
     * 设置队伍名称
     */
    @PostMapping("/rename")
    public Map<String, Object> renameTeam(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long teamId = ((Number) req.get("teamId")).longValue();
        String name = (String) req.get("name");
        
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            return Map.of("success", false, "message", "队伍不存在");
        }
        
        Team team = teamOpt.get();
        if (!team.getPlayerId().equals(playerId)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        team.setName(name);
        team.setUpdateTime(System.currentTimeMillis());
        teamRepository.save(team);
        
        return Map.of("success", true, "message", "改名成功", "team", buildTeamInfo(team));
    }
    
    /**
     * 删除队伍
     */
    @PostMapping("/delete")
    public Map<String, Object> deleteTeam(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long teamId = ((Number) req.get("teamId")).longValue();
        
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            return Map.of("success", false, "message", "队伍不存在");
        }
        
        Team team = teamOpt.get();
        if (!team.getPlayerId().equals(playerId)) {
            return Map.of("success", false, "message", "无权限");
        }
        
        teamRepository.delete(team);
        
        return Map.of("success", true, "message", "队伍已解散");
    }
    
    /**
     * 获取城池可创建的队伍数
     */
    @GetMapping("/max-teams")
    public Map<String, Object> getMaxTeams(@RequestParam Long cityId) {
        Optional<City> cityOpt = cityRepository.findById(cityId);
        if (!cityOpt.isPresent()) {
            return Map.of("success", false, "message", "城池不存在");
        }
        
        City city = cityOpt.get();
        int maxTeams = getMaxTeams(city.getBarracksLevel());
        
        return Map.of("success", true, "maxTeams", maxTeams, "barracksLevel", city.getBarracksLevel());
    }
    
    /**
     * 根据军营等级获取最大队伍数
     */
    private int getMaxTeams(Integer barracksLevel) {
        if (barracksLevel == null || barracksLevel < 1) return 1;
        if (barracksLevel > 5) return 5;
        return MAX_TEAMS_BY_BARRACKS_LEVEL[barracksLevel];
    }
    
    /**
     * 构建队伍信息
     */
    private Map<String, Object> buildTeamInfo(Team team) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", team.getId());
        info.put("playerId", team.getPlayerId());
        info.put("teamIndex", team.getTeamIndex());
        info.put("name", team.getName());
        info.put("cityId", team.getCityId());
        
        // 伙伴信息
        List<Map<String, Object>> partners = new ArrayList<>();
        addPartnerInfo(partners, team.getPartner1Id());
        addPartnerInfo(partners, team.getPartner2Id());
        addPartnerInfo(partners, team.getPartner3Id());
        info.put("partners", partners);
        
        return info;
    }
    
    private void addPartnerInfo(List<Map<String, Object>> partners, Long partnerId) {
        if (partnerId == null) {
            partners.add(null);
            return;
        }
        
        Optional<Partner> pOpt = partnerRepository.findById(partnerId);
        if (pOpt.isPresent()) {
            Partner p = pOpt.get();
            Map<String, Object> pinfo = new HashMap<>();
            pinfo.put("id", p.getId());
            pinfo.put("name", p.getName());
            pinfo.put("icon", p.getIcon());
            pinfo.put("quality", p.getQuality());
            partners.add(pinfo);
        } else {
            partners.add(Map.of("id", partnerId, "name", "未知"));
        }
    }
    
    /**
     * 计算队伍阵营加成
     */
    @GetMapping("/faction-bonus")
    public Map<String, Object> getFactionBonus(@RequestParam Long teamId) {
        Optional<Team> teamOpt = teamRepository.findById(teamId);
        if (!teamOpt.isPresent()) {
            return Map.of("success", false, "message", "队伍不存在");
        }
        
        Team team = teamOpt.get();
        
        // 获取队伍中的伙伴
        List<Partner> partners = new ArrayList<>();
        if (team.getPartner1Id() != null) {
            partnerRepository.findById(team.getPartner1Id()).ifPresent(partners::add);
        }
        if (team.getPartner2Id() != null) {
            partnerRepository.findById(team.getPartner2Id()).ifPresent(partners::add);
        }
        if (team.getPartner3Id() != null) {
            partnerRepository.findById(team.getPartner3Id()).ifPresent(partners::add);
        }
        
        // 计算阵营加成
        Map<String, Integer> bonus = factionService.calculateFactionBonus(partners);
        
        // 统计各阵营数量
        Map<String, Integer> factionCount = new HashMap<>();
        for (Partner p : partners) {
            String faction = p.getFaction();
            if (faction != null) {
                factionCount.put(faction, factionCount.getOrDefault(faction, 0) + 1);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("factionCount", factionCount);
        result.put("bonus", bonus);
        
        return result;
    }
    
    /**
     * 获取所有阵营信息
     */
    @GetMapping("/factions")
    public Map<String, Object> getFactions() {
        List<Map<String, Object>> factions = new ArrayList<>();
        for (String faction : Arrays.asList("阐教", "截教", "商朝", "周朝")) {
            factions.add(factionService.getFactionInfo(faction));
        }
        return Map.of("success", true, "factions", factions);
    }
}
