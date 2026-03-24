package com.game.controller;

import com.game.entity.ResourceField;
import com.game.service.ResourceFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/resource-field")
@CrossOrigin
public class ResourceFieldController {
    
    @Autowired
    private ResourceFieldService fieldService;
    
    // 占领资源田
    @PostMapping("/occupy")
    public Map<String, Object> occupy(Long playerId, Long cityId, Long mapZoneId, String fieldType) {
        return fieldService.occupyField(playerId, cityId, mapZoneId, fieldType);
    }
    
    // 升级资源田
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(Long playerId, Long cityId, Long fieldId) {
        return fieldService.upgradeField(playerId, cityId, fieldId);
    }
    
    // 获取玩家所有资源田
    @GetMapping("/list")
    public Map<String, Object> list(Long playerId) {
        List<ResourceField> fields = fieldService.getPlayerFields(playerId);
        Map<String, Integer> production = fieldService.calculateProduction(playerId);
        return Map.of("success", true, "fields", fields, "hourlyProduction", production);
    }
    
    // 获取单个资源田详情
    @GetMapping("/detail")
    public Map<String, Object> detail(Long fieldId) {
        ResourceField field = fieldService.getField(fieldId);
        if (field == null) {
            return Map.of("success", false, "message", "资源田不存在");
        }
        return Map.of("success", true, "field", field);
    }
    
    // 收集资源产出
    @PostMapping("/collect")
    public Map<String, Object> collect(Long playerId, Long cityId) {
        return fieldService.collectProduction(playerId, cityId);
    }
    
    // 获取每小时产出
    @GetMapping("/production")
    public Map<String, Object> production(Long playerId) {
        Map<String, Integer> production = fieldService.calculateProduction(playerId);
        return Map.of("success", true, "production", production);
    }
}