package com.game.controller;

import com.game.entity.Alliance;
import com.game.entity.AllianceMember;
import com.game.mapper.AllianceRepository;
import com.game.mapper.AllianceMemberRepository;
import com.game.mapper.AllianceMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/alliance")
@CrossOrigin
public class AllianceController {
    @Autowired
    private AllianceRepository allianceRepository;
    @Autowired
    private AllianceMemberRepository memberRepository;
    
    // 创建联盟
    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String name = (String) req.get("name");
        
        // 检查是否已有联盟
        Optional<AllianceMember> existing = memberRepository.findByPlayerId(playerId);
        if (existing.isPresent()) {
            return Map.of("success", false, "message", "已有联盟");
        }
        
        // 检查名称是否重复
        if (allianceRepository.findByName(name).isPresent()) {
            return Map.of("success", false, "message", "联盟名称已存在");
        }
        
        // 创建联盟
        Alliance alliance = new Alliance();
        alliance.setName(name);
        alliance.setLeaderId(playerId);
        alliance.setNotice("欢迎加入" + name);
        alliance.setMemberCount(1);
        alliance.setMaxMembers(20);
        alliance.setLevel(1);
        alliance.setExp(0);
        alliance.setCreateTime(System.currentTimeMillis());
        
        alliance = allianceRepository.save(alliance);
        
        // 创建盟主成员
        AllianceMember member = new AllianceMember();
        member.setAllianceId(alliance.getId());
        member.setPlayerId(playerId);
        member.setPlayerName("盟主");
        member.setRole("leader");
        member.setContribution(0);
        member.setJoinTime(System.currentTimeMillis());
        
        memberRepository.save(member);
        
        return Map.of("success", true, "alliance", alliance, "message", "联盟创建成功");
    }
    
    // 联盟列表
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Alliance> list = allianceRepository.findByIdNotNullOrderByExpDesc();
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Alliance a : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", a.getId());
            item.put("name", a.getName());
            item.put("leaderId", a.getLeaderId());
            item.put("memberCount", a.getMemberCount());
            item.put("maxMembers", a.getMaxMembers());
            item.put("level", a.getLevel());
            item.put("exp", a.getExp());
            result.add(item);
        }
        
        return Map.of("success", true, "list", result);
    }
    
    // 我的联盟
    @GetMapping("/my")
    public Map<String, Object> myAlliance(@RequestParam Long playerId) {
        Optional<AllianceMember> opt = memberRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "未加入联盟");
        }
        
        AllianceMember member = opt.get();
        Alliance alliance = allianceRepository.findById(member.getAllianceId()).orElse(null);
        
        if (alliance == null) {
            return Map.of("success", false, "message", "联盟不存在");
        }
        
        // 获取成员列表
        List<AllianceMember> members = memberRepository.findByAllianceId(alliance.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("alliance", alliance);
        result.put("members", members);
        result.put("myRole", member.getRole());
        
        return result;
    }
    
    // 加入联盟
    @PostMapping("/join")
    public Map<String, Object> join(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long allianceId = ((Number) req.get("allianceId")).longValue();
        
        // 检查是否已有联盟
        if (memberRepository.findByPlayerId(playerId).isPresent()) {
            return Map.of("success", false, "message", "已有联盟");
        }
        
        // 检查联盟是否存在
        Alliance alliance = allianceRepository.findById(allianceId).orElse(null);
        if (alliance == null) {
            return Map.of("success", false, "message", "联盟不存在");
        }
        
        // 检查是否满员
        if (alliance.getMemberCount() >= alliance.getMaxMembers()) {
            return Map.of("success", false, "message", "联盟已满");
        }
        
        // 加入联盟
        AllianceMember member = new AllianceMember();
        member.setAllianceId(allianceId);
        member.setPlayerId(playerId);
        member.setPlayerName("成员");
        member.setRole("member");
        member.setContribution(0);
        member.setJoinTime(System.currentTimeMillis());
        
        memberRepository.save(member);
        
        // 更新成员数
        alliance.setMemberCount(alliance.getMemberCount() + 1);
        allianceRepository.save(alliance);
        
        return Map.of("success", true, "message", "加入成功");
    }
    
    // 退出联盟
    @PostMapping("/leave")
    public Map<String, Object> leave(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        Optional<AllianceMember> opt = memberRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "未加入联盟");
        }
        
        AllianceMember member = opt.get();
        Alliance alliance = allianceRepository.findById(member.getAllianceId()).orElse(null);
        
        // 盟主不能退出
        if ("leader".equals(member.getRole())) {
            return Map.of("success", false, "message", "盟主无法退出，请转让盟主");
        }
        
        memberRepository.delete(member);
        
        if (alliance != null) {
            alliance.setMemberCount(alliance.getMemberCount() - 1);
            allianceRepository.save(alliance);
        }
        
        return Map.of("success", true, "message", "退出成功");
    }
    
    // 解散联盟(盟主)
    @PostMapping("/dissolve")
    public Map<String, Object> dissolve(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        Optional<AllianceMember> opt = memberRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "未加入联盟");
        }
        
        AllianceMember member = opt.get();
        if (!"leader".equals(member.getRole())) {
            return Map.of("success", false, "message", "只有盟主可以解散");
        }
        
        Alliance alliance = allianceRepository.findById(member.getAllianceId()).orElse(null);
        
        // 删除所有成员
        memberRepository.deleteAll(memberRepository.findByAllianceId(alliance.getId()));
        
        // 删除联盟
        allianceRepository.delete(alliance);
        
        return Map.of("success", true, "message", "联盟已解散");
    }
}
