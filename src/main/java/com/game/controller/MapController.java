package com.game.controller;

import com.game.entity.MapZone;
import com.game.entity.PlayerMap;
import com.game.mapper.MapZoneRepository;
import com.game.mapper.PlayerMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/map")
@CrossOrigin
public class MapController {
    @Autowired
    private MapZoneRepository mapZoneRepository;
    @Autowired
    private PlayerMapRepository playerMapRepository;
    
    // 地图区域数据
    private static final List<Map<String, Object>> ZONE_DATA = Arrays.asList(
        // 出生州
        Map.of("zoneId", 1, "name", "陈塘关", "type", "birth", "levelReq", 1, "goldOutput", 100, "lingqiOutput", 50, "relicType", "山洞", "x", 0, "y", 0),
        Map.of("zoneId", 2, "name", "西岐", "type", "birth", "levelReq", 1, "goldOutput", 100, "lingqiOutput", 50, "relicType", "古墓", "x", 1, "y", 0),
        Map.of("zoneId", 3, "name", "朝歌城郊", "type", "birth", "levelReq", 1, "goldOutput", 100, "lingqiOutput", 50, "relicType", "山洞", "x", 2, "y", 0),
        
        // 资源州
        Map.of("zoneId", 4, "name", "青龙关", "type", "resource", "levelReq", 10, "goldOutput", 300, "lingqiOutput", 150, "relicType", "仙府", "x", 0, "y", 1),
        Map.of("zoneId", 5, "name", "佳梦关", "type", "resource", "levelReq", 10, "goldOutput", 300, "lingqiOutput", 150, "relicType", "仙府", "x", 1, "y", 1),
        
        // 朝歌
        Map.of("zoneId", 6, "name", "朝歌", "type", "capital", "levelReq", 30, "goldOutput", 1000, "lingqiOutput", 500, "relicType", "无", "x", 1, "y", 2)
    );
    
    // 初始化地图数据
    @PostMapping("/init")
    public Map<String, Object> init() {
        for (Map<String, Object> data : ZONE_DATA) {
            MapZone zone = new MapZone();
            zone.setZoneId((Integer) data.get("zoneId"));
            zone.setName((String) data.get("name"));
            zone.setType((String) data.get("type"));
            zone.setLevelReq((Integer) data.get("levelReq"));
            zone.setGoldOutput((Integer) data.get("goldOutput"));
            zone.setLingqiOutput((Integer) data.get("lingqiOutput"));
            zone.setRelicType((String) data.get("relicType"));
            zone.setX((Integer) data.get("x"));
            zone.setY((Integer) data.get("y"));
            mapZoneRepository.save(zone);
        }
        return Map.of("success", true, "message", "地图初始化完成");
    }
    
    // 获取所有区域
    @GetMapping("/zones")
    public Map<String, Object> getZones() {
        List<MapZone> zones = mapZoneRepository.findAll();
        if (zones.isEmpty()) {
            // 初始化
            init();
            zones = mapZoneRepository.findAll();
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (MapZone z : zones) {
            Map<String, Object> item = new HashMap<>();
            item.put("zoneId", z.getZoneId());
            item.put("name", z.getName());
            item.put("type", z.getType());
            item.put("levelReq", z.getLevelReq());
            item.put("goldOutput", z.getGoldOutput());
            item.put("lingqiOutput", z.getLingqiOutput());
            item.put("relicType", z.getRelicType());
            item.put("x", z.getX());
            item.put("y", z.getY());
            result.add(item);
        }
        
        return Map.of("success", true, "zones", result);
    }
    
    // 获取玩家当前位置
    @GetMapping("/my")
    public Map<String, Object> myPosition(@RequestParam Long playerId) {
        Optional<PlayerMap> opt = playerMapRepository.findByPlayerId(playerId);
        
        if (!opt.isPresent()) {
            // 默认在出生州
            PlayerMap pm = new PlayerMap();
            pm.setPlayerId(playerId);
            pm.setZoneId(1);
            pm.setZoneType("birth");
            pm.setX(0);
            pm.setY(0);
            pm.setEnterTime(System.currentTimeMillis());
            pm = playerMapRepository.save(pm);
            
            return Map.of("success", true, "zoneId", 1, "zoneType", "birth", "x", 0, "y", 0);
        }
        
        PlayerMap pm = opt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("zoneId", pm.getZoneId());
        result.put("zoneType", pm.getZoneType());
        result.put("x", pm.getX());
        result.put("y", pm.getY());
        
        // 获取区域信息
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(pm.getZoneId());
        if (zoneOpt.isPresent()) {
            MapZone z = zoneOpt.get();
            result.put("zoneName", z.getName());
            result.put("relicType", z.getRelicType());
        }
        
        return result;
    }
    
    // 行军到其他区域
    @PostMapping("/move")
    public Map<String, Object> move(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer targetZoneId = (Integer) req.get("zoneId");
        
        // 获取目标区域
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(targetZoneId);
        if (!zoneOpt.isPresent()) {
            return Map.of("success", false, "message", "区域不存在");
        }
        
        MapZone target = zoneOpt.get();
        
        // 获取玩家当前位置
        Optional<PlayerMap> pmOpt = playerMapRepository.findByPlayerId(playerId);
        PlayerMap pm;
        if (pmOpt.isPresent()) {
            pm = pmOpt.get();
        } else {
            pm = new PlayerMap();
            pm.setPlayerId(playerId);
        }
        
        // 简单处理：直接移动
        pm.setZoneId(targetZoneId);
        pm.setZoneType(target.getType());
        pm.setX(target.getX());
        pm.setY(target.getY());
        pm.setEnterTime(System.currentTimeMillis());
        playerMapRepository.save(pm);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "成功进入" + target.getName());
        result.put("zoneId", targetZoneId);
        result.put("zoneName", target.getName());
        result.put("zoneType", target.getType());
        result.put("relicType", target.getRelicType());
        
        return result;
    }
    
    // 遗迹探索
    @PostMapping("/relic")
    public Map<String, Object> exploreRelic(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        // 获取玩家当前位置
        Optional<PlayerMap> pmOpt = playerMapRepository.findByPlayerId(playerId);
        if (!pmOpt.isPresent()) {
            return Map.of("success", false, "message", "请先进入地图");
        }
        
        PlayerMap pm = pmOpt.get();
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(pm.getZoneId());
        
        if (!zoneOpt.isPresent()) {
            return Map.of("success", false, "message", "区域不存在");
        }
        
        MapZone zone = zoneOpt.get();
        
        // 没有遗迹
        if ("无".equals(zone.getRelicType())) {
            return Map.of("success", false, "message", "该区域没有遗迹");
        }
        
        // 随机触发事件
        String[] events = {"获得材料", "获得坐骑", "获得法宝", "遭遇怪物", "空"};
        String event = events[(int) (Math.random() * events.length)];
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("event", event);
        result.put("zoneName", zone.getName());
        result.put("relicType", zone.getRelicType());
        
        // 根据事件发放奖励
        if ("获得材料".equals(event)) {
            result.put("reward", Map.of("type", "iron", "count", 5));
        } else if ("获得坐骑".equals(event)) {
            result.put("reward", Map.of("type", "mount", "name", "黄牛"));
        } else if ("获得法宝".equals(event)) {
            result.put("reward", Map.of("type", "treasure", "name", "火尖枪"));
        }
        
        return result;
    }
}
