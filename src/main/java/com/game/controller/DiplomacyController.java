package com.game.controller;

import com.game.entity.*;
import com.game.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/diplomacy")
@CrossOrigin
public class DiplomacyController {
    
    @Autowired
    private AllianceDiplomacyRepository diplomacyRepo;
    
    // 设置外交关系
    @PostMapping("/set")
    public Map<String, Object> set(Long allianceId1, Long allianceId2, String status) {
        if (!status.equals("friendly") && !status.equals("hostile") && !status.equals("neutral")) {
            return Map.of("success", false, "message", "状态无效");
        }
        
        Optional<AllianceDiplomacy> existing = diplomacyRepo.findByAllianceId1AndAllianceId2(allianceId1, allianceId2);
        if (existing.isPresent()) {
            AllianceDiplomacy d = existing.get();
            d.setStatus(status);
            d.setUpdateTime(System.currentTimeMillis());
            diplomacyRepo.save(d);
        } else {
            AllianceDiplomacy d = new AllianceDiplomacy();
            d.setAllianceId1(allianceId1);
            d.setAllianceId2(allianceId2);
            d.setStatus(status);
            d.setUpdateTime(System.currentTimeMillis());
            diplomacyRepo.save(d);
        }
        return Map.of("success", true, "status", status);
    }
    
    // 查看外交关系
    @GetMapping("/view")
    public Map<String, Object> view(Long allianceId1, Long allianceId2) {
        Optional<AllianceDiplomacy> d = diplomacyRepo.findByAllianceId1AndAllianceId2(allianceId1, allianceId2);
        if (!d.isPresent()) {
            return Map.of("success", true, "status", "neutral");
        }
        return Map.of("success", true, "status", d.get().getStatus());
    }
    
    // 联盟外交列表
    @GetMapping("/list")
    public Map<String, Object> list(Long allianceId) {
        List<AllianceDiplomacy> all = diplomacyRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (AllianceDiplomacy d : all) {
            if (d.getAllianceId1().equals(allianceId) || d.getAllianceId2().equals(allianceId)) {
                Map<String, Object> m = new HashMap<>();
                m.put("allianceId", d.getAllianceId1().equals(allianceId) ? d.getAllianceId2() : d.getAllianceId1());
                m.put("status", d.getStatus());
                result.add(m);
            }
        }
        return Map.of("success", true, "diplomacies", result);
    }
}
