package com.game.controller;

import com.game.entity.OfflineReward;
import com.game.entity.Player;
import com.game.mapper.PlayerRepository;
import com.game.service.OfflineRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/offline-reward")
@CrossOrigin
public class OfflineRewardController {
    
    private static final int BASE_MAX_HOURS = 12;
    
    @Autowired
    private OfflineRewardService offlineRewardService;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    /**
     * 玩家登录时调用：记录上线时间并获取离线奖励信息
     * 流程：先检查玩家 -> 记录登录/计算离线收益 -> 返回奖励信息
     */
    @PostMapping("/login")
    public Map<String, Object> onLogin(@RequestParam Long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        
        // 记录上线并计算离线收益
        offlineRewardService.recordOnline(playerId);
        
        // 获取离线奖励信息
        OfflineReward reward = offlineRewardService.getOfflineReward(playerId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("hasUnclaimedReward", reward != null && !Boolean.TRUE.equals(reward.getClaimed()) && 
                   (reward.getAccumulatedLingqi() != null && reward.getAccumulatedLingqi() > 0 || 
                    reward.getAccumulatedGold() != null && reward.getAccumulatedGold() > 0));
        result.put("lingqi", reward != null && reward.getAccumulatedLingqi() != null ? reward.getAccumulatedLingqi() : 0);
        result.put("gold", reward != null && reward.getAccumulatedGold() != null ? reward.getAccumulatedGold() : 0);
        result.put("maxOfflineHours", reward != null && reward.getMaxOfflineHours() != null ? reward.getMaxOfflineHours() : BASE_MAX_HOURS);
        
        return result;
    }
    
    /**
     * 领取离线奖励
     */
    @PostMapping("/claim")
    public Map<String, Object> claimReward(@RequestParam Long playerId) {
        return offlineRewardService.claimReward(playerId);
    }
    
    /**
     * 查询离线奖励状态（不需要登录）
     */
    @GetMapping("/status")
    public Map<String, Object> getStatus(@RequestParam Long playerId) {
        OfflineReward reward = offlineRewardService.getOfflineReward(playerId);
        
        if (reward == null) {
            return Map.of(
                "hasRecord", false,
                "maxOfflineHours", BASE_MAX_HOURS
            );
        }
        
        return Map.of(
            "hasRecord", true,
            "hasUnclaimedReward", !Boolean.TRUE.equals(reward.getClaimed()),
            "lingqi", reward.getAccumulatedLingqi() != null ? reward.getAccumulatedLingqi() : 0,
            "gold", reward.getAccumulatedGold() != null ? reward.getAccumulatedGold() : 0,
            "maxOfflineHours", reward.getMaxOfflineHours() != null ? reward.getMaxOfflineHours() : BASE_MAX_HOURS,
            "lastOnlineTime", reward.getLastOnlineTime() != null ? reward.getLastOnlineTime().toString() : null
        );
    }
    
    /**
     * 手动计算离线奖励（用于测试或GM操作）
     * 指定离线小时数，计算并累积奖励
     */
    @PostMapping("/calculate")
    public Map<String, Object> calculateReward(@RequestParam Long playerId, @RequestParam Integer hours) {
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        return offlineRewardService.calculateReward(playerId, hours);
    }
}