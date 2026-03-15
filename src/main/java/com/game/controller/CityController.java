package com.game.controller;

import com.game.entity.City;
import com.game.entity.Player;
import com.game.mapper.CityRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/city")
@CrossOrigin
public class CityController {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlayerRepository playerRepository;
    
    // 建筑数据
    private static final Map<String, int[]> BUILDING_DATA = Map.of(
        "palace", new int[]{1, 50, 30},    // 主殿: 等级, 升级金币, 升级灵气
        "barracks", new int[]{1, 40, 20},   // 兵营
        "warehouse", new int[]{1, 30, 20},  // 仓库
        "market", new int[]{1, 30, 15}      // 市场
    );
    
    // 我的城池
    @GetMapping("/my")
    public Map<String, Object> myCity(@RequestParam Long playerId) {
        Optional<City> opt = cityRepository.findByPlayerId(playerId);
        
        City city;
        if (!opt.isPresent()) {
            // 创建默认城池
            city = new City();
            city.setPlayerId(playerId);
            city.setName("陈塘关");
            city.setLevel(1);
            city.setGoldOutput(100);
            city.setLingqiOutput(50);
            city.setMorale(100);
            city.setPalaceLevel(1);
            city.setBarracksLevel(1);
            city.setWarehouseLevel(1);
            city.setMarketLevel(1);
            city = cityRepository.save(city);
        } else {
            city = opt.get();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("city", city);
        result.put("output", Map.of(
            "gold", city.getGoldOutput(),
            "lingqi", city.getLingqiOutput()
        ));
        
        return result;
    }
    
    // 升级建筑
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String building = (String) req.get("building"); // palace/barracks/warehouse/market
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        Optional<City> opt = cityRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "城池不存在");
        }
        
        City city = opt.get();
        int[] data = BUILDING_DATA.get(building);
        if (data == null) {
            return Map.of("success", false, "message", "无效建筑");
        }
        
        // 获取当前等级
        int currentLevel;
        switch (building) {
            case "palace": currentLevel = city.getPalaceLevel(); break;
            case "barracks": currentLevel = city.getBarracksLevel(); break;
            case "warehouse": currentLevel = city.getWarehouseLevel(); break;
            case "market": currentLevel = city.getMarketLevel(); break;
            default: return Map.of("success", false, "message", "无效建筑");
        }
        
        // 计算升级消耗
        int goldCost = data[1] * currentLevel;
        int lingqiCost = data[2] * currentLevel;
        
        // 检查资源是否足够
        if (player.getGold() < goldCost) {
            return Map.of("success", false, "message", "金币不足，需要" + goldCost);
        }
        if (player.getLingqi() < lingqiCost) {
            return Map.of("success", false, "message", "灵气不足，需要" + lingqiCost);
        }
        
        // 扣除资源
        player.setGold(player.getGold() - goldCost);
        player.setLingqi(player.getLingqi() - lingqiCost);
        playerRepository.save(player);
        
        // 升级建筑
        switch (building) {
            case "palace": city.setPalaceLevel(currentLevel + 1); break;
            case "barracks": city.setBarracksLevel(currentLevel + 1); break;
            case "warehouse": city.setWarehouseLevel(currentLevel + 1); break;
            case "market": city.setMarketLevel(currentLevel + 1); break;
        }
        
        // 更新产出
        city.setGoldOutput(city.getGoldOutput() + 20 * currentLevel);
        city.setLingqiOutput(city.getLingqiOutput() + 10 * currentLevel);
        
        cityRepository.save(city);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", building + "升级到" + (currentLevel + 1));
        result.put("city", city);
        result.put("gold", player.getGold());
        result.put("lingqi", player.getLingqi());
        
        return result;
    }
    
    // 征收(获得资源)
    @PostMapping("/collect")
    public Map<String, Object> collect(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        Optional<City> opt = cityRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "城池不存在");
        }
        
        City city = opt.get();
        
        // 征收获得资源
        int gold = city.getGoldOutput();
        int lingqi = city.getLingqiOutput();
        
        // 士气影响产出
        double moraleFactor = city.getMorale() / 100.0;
        gold = (int) (gold * moraleFactor);
        lingqi = (int) (lingqi * moraleFactor);
        
        // 发放资源
        player.setGold(player.getGold() + gold);
        player.setLingqi(player.getLingqi() + lingqi);
        playerRepository.save(player);
        
        return Map.of("success", true, "gold", gold, "lingqi", lingqi, 
            "totalGold", player.getGold(), "totalLingqi", player.getLingqi(),
            "message", "征收成功");
    }
    
    // 民心/士气
    @PostMapping("/morale")
    public Map<String, Object> morale(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer change = (Integer) req.getOrDefault("change", 0); // 正数增加，负数减少
        
        Optional<City> opt = cityRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "城池不存在");
        }
        
        City city = opt.get();
        int newMorale = Math.max(0, Math.min(100, city.getMorale() + change));
        city.setMorale(newMorale);
        cityRepository.save(city);
        
        return Map.of("success", true, "morale", newMorale, "message", 
            change > 0 ? "民心增加" : "民心减少");
    }
}
