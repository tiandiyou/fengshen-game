package com.game.controller;

import com.game.entity.*;
import com.game.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/alliance")
@CrossOrigin
public class AllianceController {
    
    @Autowired
    private AllianceRepository allianceRepo;
    
    @Autowired
    private AllianceMemberRepository memberRepo;
    
    // 创建联盟
    @PostMapping("/create")
    public Map<String, Object> create(Long playerId, String name) {
        if (allianceRepo.findByName(name).isPresent()) {
            return Map.of("success", false, "message", "联盟名已存在");
        }
        Alliance a = new Alliance();
        a.setName(name);
        a.setIcon("⚔️");
        a.setLeaderId(playerId);
        a.setLevel(1);
        a.setMemberCount(1);
        a.setCreateTime(System.currentTimeMillis());
        allianceRepo.save(a);
        
        AllianceMember m = new AllianceMember();
        m.setAllianceId(a.getId());
        m.setPlayerId(playerId);
        m.setRole("leader");
        m.setJoinTime(System.currentTimeMillis());
        memberRepo.save(m);
        
        return Map.of("success", true, "alliance", a);
    }
    
    // 联盟列表
    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Alliance> alliances = allianceRepo.findAll();
        return Map.of("success", true, "alliances", alliances);
    }
    
    // 申请加入
    @PostMapping("/join")
    public Map<String, Object> join(Long playerId, Long allianceId) {
        if (memberRepo.findByAllianceIdAndPlayerId(allianceId, playerId).isPresent()) {
            return Map.of("success", false, "message", "已在联盟中");
        }
        Alliance a = allianceRepo.findById(allianceId).orElse(null);
        if (a == null) {
            return Map.of("success", false, "message", "联盟不存在");
        }
        if (a.getMemberCount() >= 20) {
            return Map.of("success", false, "message", "联盟已满");
        }
        AllianceMember m = new AllianceMember();
        m.setAllianceId(allianceId);
        m.setPlayerId(playerId);
        m.setRole("member");
        m.setJoinTime(System.currentTimeMillis());
        memberRepo.save(m);
        
        a.setMemberCount(a.getMemberCount() + 1);
        allianceRepo.save(a);
        
        return Map.of("success", true, "message", "加入成功");
    }
    
    // 我的联盟
    @GetMapping("/my")
    public Map<String, Object> my(Long playerId) {
        Optional<AllianceMember> om = memberRepo.findByPlayerId(playerId);
        if (!om.isPresent()) {
            return Map.of("success", false, "message", "未加入联盟");
        }
        Alliance a = allianceRepo.findById(om.get().getAllianceId()).orElse(null);
        if (a == null) {
            return Map.of("success", false, "message", "联盟不存在");
        }
        List<AllianceMember> members = memberRepo.findByAllianceId(a.getId());
        return Map.of("success", true, "alliance", a, "members", members);
    }
}
