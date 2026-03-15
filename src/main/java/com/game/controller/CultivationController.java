package com.game.controller;

import com.game.entity.Cultivation;
import com.game.entity.Player;
import com.game.mapper.CultivationRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/cultivation")
@CrossOrigin
public class CultivationController {
    @Autowired
    private CultivationRepository cultivationRepository;
    @Autowired
    private PlayerRepository playerRepository;
    
    // 修仙类型
    private static final String TYPE_BODY = "body";  // 体修
    private static final String TYPE_MAGIC = "magic"; // 法修
    
    // 最大等级
    private static final int MAX_LEVEL = 50;
    
    // 升级所需经验(每级100点)
    private static int getExpForLevel(int level) {
        return level * 100;
    }
    
    // 获取修仙信息
    @GetMapping("/info")
    public Map<String, Object> getInfo(@RequestParam Long playerId) {
        Optional<Cultivation> opt = cultivationRepository.findByPlayerId(playerId);
        
        // 如果没有修仙记录，创建新的
        Cultivation cultivation;
        if (!opt.isPresent()) {
            cultivation = new Cultivation();
            cultivation.setPlayerId(playerId);
            cultivation.setType(TYPE_BODY); // 默认体修
            cultivation.setLevel(1);
            cultivation.setExp(0);
            cultivation.setTotalTime(0L);
            cultivation.setLastUpdate(System.currentTimeMillis());
            cultivation = cultivationRepository.save(cultivation);
        } else {
            cultivation = opt.get();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("type", cultivation.getType());
        result.put("level", cultivation.getLevel());
        result.put("exp", cultivation.getExp());
        result.put("expForNext", getExpForLevel(cultivation.getLevel()));
        result.put("maxLevel", MAX_LEVEL);
        result.put("bonus", cultivation.getLevel() * 5); // 属性加成百分比
        result.put("totalTime", cultivation.getTotalTime());
        
        return result;
    }
    
    // 选择流派
    @PostMapping("/choose")
    public Map<String, Object> chooseType(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String type = (String) req.get("type");
        
        if (!TYPE_BODY.equals(type) && !TYPE_MAGIC.equals(type)) {
            return Map.of("success", false, "message", "无效的流派");
        }
        
        Optional<Cultivation> opt = cultivationRepository.findByPlayerId(playerId);
        Cultivation cultivation;
        if (opt.isPresent()) {
            cultivation = opt.get();
            if (cultivation.getLevel() > 1) {
                return Map.of("success", false, "message", "已选择流派，无法更改");
            }
            cultivation.setType(type);
        } else {
            cultivation = new Cultivation();
            cultivation.setPlayerId(playerId);
            cultivation.setType(type);
            cultivation.setLevel(1);
            cultivation.setExp(0);
            cultivation.setTotalTime(0L);
            cultivation.setLastUpdate(System.currentTimeMillis());
        }
        
        cultivationRepository.save(cultivation);
        return Map.of("success", true, "type", type, "message", "选择成功");
    }
    
    // 修炼(挂机获得经验)
    @PostMapping("/practice")
    public Map<String, Object> practice(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer seconds = (Integer) req.getOrDefault("seconds", 60); // 默认修炼60秒
        
        Optional<Cultivation> opt = cultivationRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "请先选择流派");
        }
        
        Cultivation cultivation = opt.get();
        if (cultivation.getLevel() >= MAX_LEVEL) {
            return Map.of("success", false, "message", "已达满级");
        }
        
        // 计算获得经验: 1秒=1点经验
        int expGain = seconds;
        cultivation.setExp(cultivation.getExp() + expGain);
        cultivation.setTotalTime(cultivation.getTotalTime() + seconds);
        
        // 检查升级
        int expNeeded = getExpForLevel(cultivation.getLevel());
        while (cultivation.getExp() >= expNeeded && cultivation.getLevel() < MAX_LEVEL) {
            cultivation.setExp(cultivation.getExp() - expNeeded);
            cultivation.setLevel(cultivation.getLevel() + 1);
            expNeeded = getExpForLevel(cultivation.getLevel());
        }
        
        cultivation.setLastUpdate(System.currentTimeMillis());
        cultivationRepository.save(cultivation);
        
        // 更新玩家属性加成
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player != null) {
            int bonus = cultivation.getLevel() * 5;
            // 体修增加武将属性，法修增加技能伤害
            // 这里简单处理，实际应该计算到战力中
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("level", cultivation.getLevel());
        result.put("exp", cultivation.getExp());
        result.put("expForNext", getExpForLevel(cultivation.getLevel()));
        result.put("bonus", cultivation.getLevel() * 5);
        result.put("expGain", expGain);
        
        return result;
    }
    
    // 加速升级(使用结晶)
    @PostMapping("/accelerate")
    public Map<String, Object> accelerate(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer crystals = (Integer) req.getOrDefault("crystals", 1); // 消耗结晶数量
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        // 检查结晶是否足够
        // 简化: 用灵气代替结晶
        if (player.getLingqi() < crystals * 100) {
            return Map.of("success", false, "message", "灵气不足");
        }
        
        Optional<Cultivation> opt = cultivationRepository.findByPlayerId(playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "请先选择流派");
        }
        
        Cultivation cultivation = opt.get();
        if (cultivation.getLevel() >= MAX_LEVEL) {
            return Map.of("success", false, "message", "已达满级");
        }
        
        // 消耗灵气
        player.setLingqi(player.getLingqi() - crystals * 100);
        
        // 直接升1级
        cultivation.setLevel(cultivation.getLevel() + 1);
        cultivation.setExp(0);
        cultivationRepository.save(cultivation);
        playerRepository.save(player);
        
        return Map.of("success", true, "level", cultivation.getLevel(), "bonus", cultivation.getLevel() * 5);
    }
}
