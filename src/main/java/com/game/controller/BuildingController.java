package com.game.controller;

import com.game.entity.Building;
import com.game.mapper.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/building")
@CrossOrigin
public class BuildingController {
    
    @Autowired
    private BuildingRepository buildingRepo;
    
    // 初始化建筑
    @PostMapping("/init")
    public Map<String, Object> init(Long cityId) {
        List<Building> existing = buildingRepo.findByCityId(cityId);
        if (!existing.isEmpty()) {
            return Map.of("success", false, "message", "已存在");
        }
        String[] types = {"palace", "warehouse", "barracks", "market", "farm", "wood", "iron"};
        for (String type : types) {
            Building b = new Building();
            b.setCityId(cityId);
            b.setType(type);
            b.setLevel(1);
            b.setUpdateTime(System.currentTimeMillis());
            buildingRepo.save(b);
        }
        return Map.of("success", true, "message", "建筑初始化成功");
    }
    
    // 获取建筑列表
    @GetMapping("/list")
    public Map<String, Object> list(Long cityId) {
        List<Building> buildings = buildingRepo.findByCityId(cityId);
        if (buildings.isEmpty()) {
            init(cityId);
            buildings = buildingRepo.findByCityId(cityId);
        }
        return Map.of("success", true, "buildings", buildings);
    }
    
    // 升级建筑
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(Long cityId, String type) {
        Optional<Building> ob = buildingRepo.findByCityIdAndType(cityId, type);
        if (!ob.isPresent()) {
            return Map.of("success", false, "message", "建筑不存在");
        }
        Building b = ob.get();
        b.setLevel(b.getLevel() + 1);
        b.setUpdateTime(System.currentTimeMillis());
        buildingRepo.save(b);
        return Map.of("success", true, "type", type, "level", b.getLevel());
    }
    
    // 获取产出加成
    @GetMapping("/bonus")
    public Map<String, Object> bonus(Long cityId) {
        List<Building> buildings = buildingRepo.findByCityId(cityId);
        Map<String, Integer> bonus = new HashMap<>();
        for (Building b : buildings) {
            bonus.put(b.getType() + "Level", b.getLevel());
        }
        return Map.of("success", true, "bonus", bonus);
    }
}
