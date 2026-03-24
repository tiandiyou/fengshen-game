package com.game.service;

import com.game.entity.Resource;
import com.game.entity.ResourceField;
import com.game.mapper.ResourceFieldRepository;
import com.game.mapper.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ResourceFieldService {
    
    @Autowired
    private ResourceFieldRepository fieldRepo;
    
    @Autowired
    private ResourceRepository resourceRepo;
    
    // 资源田类型配置
    private static final Map<String, Integer[]> LEVEL_CONFIGS = Map.of(
        "farm", new Integer[]{50, 100, 150, 200, 250, 300, 350, 400, 450, 500},    // 农田产出/小时
        "lumber", new Integer[]{40, 80, 120, 160, 200, 240, 280, 320, 360, 400},  // 伐木场产出/小时
        "iron", new Integer[]{30, 60, 90, 120, 150, 180, 210, 240, 270, 300},     // 铁矿产出/小时
        "gold", new Integer[]{20, 40, 60, 80, 100, 120, 140, 160, 180, 200}      // 金矿产出/小时
    );
    
    // 升级消耗配置 (wood, iron, gold)
    private static final Map<Integer, Integer[]> UPGRADE_COSTS = Map.of(
        2, new Integer[]{100, 50, 0},
        3, new Integer[]{200, 100, 10},
        4, new Integer[]{400, 200, 30},
        5, new Integer[]{800, 400, 50},
        6, new Integer[]{1200, 600, 100},
        7, new Integer[]{2000, 1000, 200},
        8, new Integer[]{3000, 1500, 300},
        9, new Integer[]{5000, 2500, 500},
        10, new Integer[]{8000, 4000, 800}
    );
    
    // 占领资源田
    public Map<String, Object> occupyField(Long playerId, Long cityId, Long mapZoneId, String fieldType) {
        // 检查是否已有该区域的资源田
        Optional<ResourceField> existing = fieldRepo.findByPlayerIdAndMapZoneId(playerId, mapZoneId);
        if (existing.isPresent()) {
            return Map.of("success", false, "message", "该区域已有资源田");
        }
        
        // 检查资源是否足够
        Optional<Resource> ro = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        if (!ro.isPresent()) {
            return Map.of("success", false, "message", "请先初始化资源");
        }
        Resource r = ro.get();
        
        // 占领消耗 (木材100, 铁矿50)
        if (r.getWood() < 100 || r.getIron() < 50) {
            return Map.of("success", false, "message", "资源不足，需要木材100，铁矿50");
        }
        
        // 扣除资源
        r.setWood(r.getWood() - 100);
        r.setIron(r.getIron() - 50);
        resourceRepo.save(r);
        
        // 创建资源田
        ResourceField field = new ResourceField();
        field.setPlayerId(playerId);
        field.setMapZoneId(mapZoneId);
        field.setFieldType(fieldType);
        field.setLevel(1);
        field.setCreateTime(System.currentTimeMillis());
        field.setUpdateTime(System.currentTimeMillis());
        fieldRepo.save(field);
        
        return Map.of("success", true, "message", "资源田占领成功", "field", field);
    }
    
    // 升级资源田
    public Map<String, Object> upgradeField(Long playerId, Long cityId, Long fieldId) {
        Optional<ResourceField> fo = fieldRepo.findById(fieldId);
        if (!fo.isPresent()) {
            return Map.of("success", false, "message", "资源田不存在");
        }
        ResourceField field = fo.get();
        
        if (!field.getPlayerId().equals(playerId)) {
            return Map.of("success", false, "message", "无权限操作");
        }
        
        int newLevel = field.getLevel() + 1;
        if (newLevel > 10) {
            return Map.of("success", false, "message", "已达最高等级");
        }
        
        // 获取升级消耗
        Integer[] costs = UPGRADE_COSTS.get(newLevel);
        if (costs == null) {
            return Map.of("success", false, "message", "配置错误");
        }
        
        // 检查资源
        Optional<Resource> ro = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        if (!ro.isPresent()) {
            return Map.of("success", false, "message", "请先初始化资源");
        }
        Resource res = ro.get();
        
        if (res.getWood() < costs[0] || res.getIron() < costs[1] || res.getGold() < costs[2]) {
            return Map.of("success", false, "message", 
                String.format("资源不足，需要木材%d，铁矿%d，金币%d", costs[0], costs[1], costs[2]));
        }
        
        // 扣除资源
        res.setWood(res.getWood() - costs[0]);
        res.setIron(res.getIron() - costs[1]);
        res.setGold(res.getGold() - costs[2]);
        resourceRepo.save(res);
        
        // 升级
        field.setLevel(newLevel);
        field.setUpdateTime(System.currentTimeMillis());
        fieldRepo.save(field);
        
        return Map.of("success", true, "message", "升级成功", "level", newLevel, "field", field);
    }
    
    // 获取玩家所有资源田
    public List<ResourceField> getPlayerFields(Long playerId) {
        return fieldRepo.findByPlayerId(playerId);
    }
    
    // 计算资源田产出
    public Map<String, Integer> calculateProduction(Long playerId) {
        List<ResourceField> fields = fieldRepo.findByPlayerId(playerId);
        Map<String, Integer> production = new HashMap<>();
        production.put("food", 0);
        production.put("wood", 0);
        production.put("iron", 0);
        production.put("gold", 0);
        
        for (ResourceField field : fields) {
            Integer[] output = LEVEL_CONFIGS.get(field.getFieldType());
            if (output != null && field.getLevel() <= output.length) {
                int amount = output[field.getLevel() - 1];
                switch (field.getFieldType()) {
                    case "farm": production.put("food", production.get("food") + amount); break;
                    case "lumber": production.put("wood", production.get("wood") + amount); break;
                    case "iron": production.put("iron", production.get("iron") + amount); break;
                    case "gold": production.put("gold", production.get("gold") + amount); break;
                }
            }
        }
        
        return production;
    }
    
    // 收集资源田产出
    public Map<String, Object> collectProduction(Long playerId, Long cityId) {
        // 查找玩家的资源田
        List<ResourceField> fields = fieldRepo.findByPlayerId(playerId);
        if (fields.isEmpty()) {
            return Map.of("success", false, "message", "暂无资源田");
        }
        
        // 计算产出
        Map<String, Integer> production = calculateProduction(playerId);
        
        // 获取或创建资源
        Optional<Resource> ro = resourceRepo.findByPlayerIdAndCityId(playerId, cityId);
        Resource res;
        if (!ro.isPresent()) {
            res = new Resource();
            res.setPlayerId(playerId);
            res.setCityId(cityId);
            res.setFood(0);
            res.setWood(0);
            res.setIron(0);
            res.setGold(0);
        } else {
            res = ro.get();
        }
        
        // 添加产出
        res.setFood(res.getFood() + production.get("food"));
        res.setWood(res.getWood() + production.get("wood"));
        res.setIron(res.getIron() + production.get("iron"));
        res.setGold(res.getGold() + production.get("gold"));
        res.setUpdateTime(System.currentTimeMillis());
        resourceRepo.save(res);
        
        return Map.of("success", true, "message", "资源收集成功", 
            "collected", production, 
            "total", Map.of("food", res.getFood(), "wood", res.getWood(), "iron", res.getIron(), "gold", res.getGold()));
    }
    
    // 获取资源田详情
    public ResourceField getField(Long fieldId) {
        return fieldRepo.findById(fieldId).orElse(null);
    }
}