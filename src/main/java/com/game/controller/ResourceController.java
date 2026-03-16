package com.game.controller;

import com.game.entity.Resource;
import com.game.mapper.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/resource")
@CrossOrigin
public class ResourceController {
    
    @Autowired
    private ResourceRepository resourceRepo;
    
    // 初始化资源
    @PostMapping("/init")
    public Map<String, Object> init(Long playerId, Long cityId) {
        Optional<Resource> existing = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        if (existing.isPresent()) {
            return Map.of("success", false, "message", "已存在");
        }
        Resource r = new Resource();
        r.setPlayerId(playerId);
        r.setCityId(cityId);
        r.setFood(10000);
        r.setWood(10000);
        r.setIron(10000);
        r.setGold(10000);
        r.setUpdateTime(System.currentTimeMillis());
        resourceRepo.save(r);
        return Map.of("success", true, "message", "资源初始化成功");
    }
    
    // 获取资源
    @GetMapping("/my")
    public Map<String, Object> get(Long playerId, Long cityId) {
        Optional<Resource> r = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        if (!r.isPresent()) {
            return init(playerId, cityId);
        }
        return Map.of("success", true, "resource", r.get());
    }
    
    // 产出
    @PostMapping("/produce")
    public Map<String, Object> produce(Long playerId, Long cityId) {
        Optional<Resource> ro = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        if (!ro.isPresent()) {
            return init(playerId, cityId);
        }
        Resource r = ro.get();
        // 基础产出
        r.setFood(r.getFood() + 100);
        r.setWood(r.getWood() + 80);
        r.setIron(r.getIron() + 60);
        r.setUpdateTime(System.currentTimeMillis());
        resourceRepo.save(r);
        return Map.of("success", true, "food", r.getFood(), "wood", r.getWood(), "iron", r.getIron());
    }
}
