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
    
    // 地图区域数据 - 使用 HashMap 避免 Map.of 参数限制
    private static final List<Map<String, Object>> ZONE_DATA;
    
    static {
        ZONE_DATA = new ArrayList<>();
        
        // === 出生州 ===
        Map<String, Object> zone1 = new HashMap<>();
        zone1.put("zoneId", 1);
        zone1.put("name", "陈塘关");
        zone1.put("type", "birth");
        zone1.put("levelReq", 1);
        zone1.put("goldOutput", 100);
        zone1.put("lingqiOutput", 50);
        zone1.put("relicType", "山洞");
        zone1.put("x", 0);
        zone1.put("y", 0);
        zone1.put("climate", "sunny");
        zone1.put("monster", "海妖");
        zone1.put("monsterLevel", 1);
        zone1.put("defense", 0);
        ZONE_DATA.add(zone1);
        
        Map<String, Object> zone2 = new HashMap<>();
        zone2.put("zoneId", 2);
        zone2.put("name", "西岐");
        zone2.put("type", "birth");
        zone2.put("levelReq", 1);
        zone2.put("goldOutput", 100);
        zone2.put("lingqiOutput", 50);
        zone2.put("relicType", "古墓");
        zone2.put("x", 1);
        zone2.put("y", 0);
        zone2.put("climate", "sunny");
        zone2.put("monster", "狼妖");
        zone2.put("monsterLevel", 1);
        zone2.put("defense", 0);
        ZONE_DATA.add(zone2);
        
        Map<String, Object> zone3 = new HashMap<>();
        zone3.put("zoneId", 3);
        zone3.put("name", "朝歌城郊");
        zone3.put("type", "birth");
        zone3.put("levelReq", 1);
        zone3.put("goldOutput", 100);
        zone3.put("lingqiOutput", 50);
        zone3.put("relicType", "山洞");
        zone3.put("x", 2);
        zone3.put("y", 0);
        zone3.put("climate", "rainy");
        zone3.put("monster", "鬼魂");
        zone3.put("monsterLevel", 2);
        zone3.put("defense", 0);
        ZONE_DATA.add(zone3);
        
        // === 资源州 ===
        Map<String, Object> zone4 = new HashMap<>();
        zone4.put("zoneId", 4);
        zone4.put("name", "青龙关");
        zone4.put("type", "resource");
        zone4.put("levelReq", 10);
        zone4.put("goldOutput", 300);
        zone4.put("lingqiOutput", 150);
        zone4.put("relicType", "仙府");
        zone4.put("x", 0);
        zone4.put("y", 1);
        zone4.put("climate", "sunny");
        zone4.put("monster", "虎怪");
        zone4.put("monsterLevel", 10);
        zone4.put("defense", 100);
        ZONE_DATA.add(zone4);
        
        Map<String, Object> zone5 = new HashMap<>();
        zone5.put("zoneId", 5);
        zone5.put("name", "佳梦关");
        zone5.put("type", "resource");
        zone5.put("levelReq", 10);
        zone5.put("goldOutput", 300);
        zone5.put("lingqiOutput", 150);
        zone5.put("relicType", "仙府");
        zone5.put("x", 1);
        zone5.put("y", 1);
        zone5.put("climate", "rainy");
        zone5.put("monster", "蛇妖");
        zone5.put("monsterLevel", 12);
        zone5.put("defense", 100);
        ZONE_DATA.add(zone5);
        
        Map<String, Object> zone7 = new HashMap<>();
        zone7.put("zoneId", 7);
        zone7.put("name", "汜水关");
        zone7.put("type", "resource");
        zone7.put("levelReq", 15);
        zone7.put("goldOutput", 400);
        zone7.put("lingqiOutput", 200);
        zone7.put("relicType", "古墓");
        zone7.put("x", 2);
        zone7.put("y", 1);
        zone7.put("climate", "sunny");
        zone7.put("monster", "石魔");
        zone7.put("monsterLevel", 15);
        zone7.put("defense", 150);
        ZONE_DATA.add(zone7);
        
        // === 特殊区域 ===
        Map<String, Object> zone8 = new HashMap<>();
        zone8.put("zoneId", 8);
        zone8.put("name", "火云洞");
        zone8.put("type", "special");
        zone8.put("levelReq", 20);
        zone8.put("goldOutput", 600);
        zone8.put("lingqiOutput", 300);
        zone8.put("relicType", "仙府");
        zone8.put("x", 0);
        zone8.put("y", 2);
        zone8.put("climate", "sunny");
        zone8.put("monster", "火精灵");
        zone8.put("monsterLevel", 20);
        zone8.put("defense", 300);
        ZONE_DATA.add(zone8);
        
        Map<String, Object> zone9 = new HashMap<>();
        zone9.put("zoneId", 9);
        zone9.put("name", "骷髅山");
        zone9.put("type", "special");
        zone9.put("levelReq", 25);
        zone9.put("goldOutput", 800);
        zone9.put("lingqiOutput", 400);
        zone9.put("relicType", "山洞");
        zone9.put("x", 2);
        zone9.put("y", 2);
        zone9.put("climate", "rainy");
        zone9.put("monster", "骷髅王");
        zone9.put("monsterLevel", 25);
        zone9.put("defense", 400);
        ZONE_DATA.add(zone9);
        
        // === 朝歌（首都）===
        Map<String, Object> zone6 = new HashMap<>();
        zone6.put("zoneId", 6);
        zone6.put("name", "朝歌");
        zone6.put("type", "capital");
        zone6.put("levelReq", 30);
        zone6.put("goldOutput", 1000);
        zone6.put("lingqiOutput", 500);
        zone6.put("relicType", "无");
        zone6.put("x", 1);
        zone6.put("y", 2);
        zone6.put("climate", "sunny");
        zone6.put("monster", "禁卫");
        zone6.put("monsterLevel", 30);
        zone6.put("defense", 1000);
        ZONE_DATA.add(zone6);
    }
    
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
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "地图初始化完成");
        return result;
    }
    
    // 获取所有区域
    @GetMapping("/zones")
    public Map<String, Object> getZones() {
        List<MapZone> zones = mapZoneRepository.findAll();
        if (zones.isEmpty()) {
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
        
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("zones", result);
        return res;
    }
    
    // 获取玩家当前位置
    @GetMapping("/my")
    public Map<String, Object> myPosition(@RequestParam Long playerId) {
        Optional<PlayerMap> opt = playerMapRepository.findByPlayerId(playerId);
        
        if (!opt.isPresent()) {
            PlayerMap pm = new PlayerMap();
            pm.setPlayerId(playerId);
            pm.setZoneId(1);
            pm.setZoneType("birth");
            pm.setX(0);
            pm.setY(0);
            pm.setEnterTime(System.currentTimeMillis());
            pm = playerMapRepository.save(pm);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("zoneId", 1);
            result.put("zoneType", "birth");
            result.put("x", 0);
            result.put("y", 0);
            return result;
        }
        
        PlayerMap pm = opt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("zoneId", pm.getZoneId());
        result.put("zoneType", pm.getZoneType());
        result.put("x", pm.getX());
        result.put("y", pm.getY());
        
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(pm.getZoneId());
        if (zoneOpt.isPresent()) {
            MapZone z = zoneOpt.get();
            result.put("zoneName", z.getName());
            result.put("relicType", z.getRelicType());
            result.put("climate", z.getClimate());
            result.put("monster", z.getMonster());
            result.put("monsterLevel", z.getMonsterLevel());
        }
        
        return result;
    }
    
    // 获取区域详情
    @GetMapping("/zone/{zoneId}")
    public Map<String, Object> getZoneDetail(@PathVariable Integer zoneId) {
        Optional<MapZone> opt = mapZoneRepository.findByZoneId(zoneId);
        
        if (!opt.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "区域不存在");
            return result;
        }
        
        MapZone z = opt.get();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("zoneId", z.getZoneId());
        result.put("name", z.getName());
        result.put("type", z.getType());
        result.put("levelReq", z.getLevelReq());
        result.put("goldOutput", z.getGoldOutput());
        result.put("lingqiOutput", z.getLingqiOutput());
        result.put("relicType", z.getRelicType());
        result.put("x", z.getX());
        result.put("y", z.getY());
        result.put("climate", z.getClimate());
        result.put("monster", z.getMonster());
        result.put("monsterLevel", z.getMonsterLevel());
        result.put("defense", z.getDefense());
        result.put("ownerId", z.getOwnerId());
        
        return result;
    }
    
    // 移动到其他区域
    @PostMapping("/travel")
    public Map<String, Object> move(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer targetZoneId = (Integer) req.get("zoneId");
        
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(targetZoneId);
        if (!zoneOpt.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "区域不存在");
            return result;
        }
        
        MapZone target = zoneOpt.get();
        
        Optional<PlayerMap> pmOpt = playerMapRepository.findByPlayerId(playerId);
        PlayerMap pm;
        if (pmOpt.isPresent()) {
            pm = pmOpt.get();
        } else {
            pm = new PlayerMap();
            pm.setPlayerId(playerId);
        }
        
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
        
        Optional<PlayerMap> pmOpt = playerMapRepository.findByPlayerId(playerId);
        if (!pmOpt.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "请先进入地图");
            return result;
        }
        
        PlayerMap pm = pmOpt.get();
        Optional<MapZone> zoneOpt = mapZoneRepository.findByZoneId(pm.getZoneId());
        
        if (!zoneOpt.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "区域不存在");
            return result;
        }
        
        MapZone zone = zoneOpt.get();
        
        if ("无".equals(zone.getRelicType())) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "该区域没有遗迹");
            return result;
        }
        
        String[] events = {"获得材料", "获得坐骑", "获得法宝", "遭遇怪物", "空"};
        String event = events[(int) (Math.random() * events.length)];
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("event", event);
        result.put("zoneName", zone.getName());
        result.put("relicType", zone.getRelicType());
        
        if ("获得材料".equals(event)) {
            Map<String, Object> reward = new HashMap<>();
            reward.put("type", "iron");
            reward.put("count", 5);
            result.put("reward", reward);
        } else if ("获得坐骑".equals(event)) {
            Map<String, Object> reward = new HashMap<>();
            reward.put("type", "mount");
            reward.put("name", "黄牛");
            result.put("reward", reward);
        } else if ("获得法宝".equals(event)) {
            Map<String, Object> reward = new HashMap<>();
            reward.put("type", "treasure");
            reward.put("name", "火尖枪");
            result.put("reward", reward);
        }
        
        return result;
    }
}
