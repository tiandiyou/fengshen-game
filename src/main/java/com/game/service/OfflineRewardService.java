package com.game.service;

import com.game.entity.OfflineReward;
import com.game.entity.Player;
import com.game.mapper.OfflineRewardRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class OfflineRewardService {
    
    @Autowired
    private OfflineRewardRepository offlineRewardRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    // 每小时基础产出
    private static final int BASE_LINGQI_PER_HOUR = 20;
    private static final int BASE_GOLD_PER_HOUR = 100;
    
    // 基础离线上限：12小时
    private static final int BASE_MAX_HOURS = 12;
    
    /**
     * 记录玩家登录（初始化/更新离线奖励记录）
     */
    public void recordOnline(Long playerId) {
        OfflineReward record = offlineRewardRepository.findByPlayerId(playerId).orElse(null);
        
        if (record == null) {
            record = new OfflineReward();
            record.setPlayerId(playerId);
            record.setAccumulatedLingqi(0);
            record.setAccumulatedGold(0);
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 如果有未领取的累积，计算新产出
        if (record.getLastOnlineTime() != null && !Boolean.TRUE.equals(record.getClaimed())) {
            long hours = ChronoUnit.HOURS.between(record.getLastOnlineTime(), now);
            if (hours > 0) {
                int maxHours = BASE_MAX_HOURS;
                int cappedHours = (int) Math.min(hours, maxHours);
                
                record.setAccumulatedLingqi(record.getAccumulatedLingqi() + BASE_LINGQI_PER_HOUR * cappedHours);
                record.setAccumulatedGold(record.getAccumulatedGold() + BASE_GOLD_PER_HOUR * cappedHours);
            }
        }
        
        record.setLastOnlineTime(now);
        record.setMaxOfflineHours(BASE_MAX_HOURS);
        record.setClaimed(false);
        record.setUpdatedAt(now);
        
        offlineRewardRepository.save(record);
    }
    
    /**
     * 获取离线奖励信息
     */
    public OfflineReward getOfflineReward(Long playerId) {
        return offlineRewardRepository.findByPlayerId(playerId).orElse(null);
    }
    
    /**
     * 领取离线奖励
     */
    public Map<String, Object> claimReward(Long playerId) {
        OfflineReward record = offlineRewardRepository.findByPlayerId(playerId).orElse(null);
        
        if (record == null || Boolean.TRUE.equals(record.getClaimed())) {
            return Map.of("success", false, "message", "无可领取的离线奖励");
        }
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) {
            return Map.of("success", false, "message", "玩家不存在");
        }
        
        int lingqi = record.getAccumulatedLingqi() != null ? record.getAccumulatedLingqi() : 0;
        int gold = record.getAccumulatedGold() != null ? record.getAccumulatedGold() : 0;
        
        if (lingqi == 0 && gold == 0) {
            return Map.of("success", false, "message", "无可领取的离线奖励");
        }
        
        // 发放奖励
        player.setLingqi(player.getLingqi() + lingqi);
        player.setGold(player.getGold() + gold);
        playerRepository.save(player);
        
        // 标记已领取，重置累积
        record.setClaimed(true);
        record.setAccumulatedLingqi(0);
        record.setAccumulatedGold(0);
        record.setUpdatedAt(LocalDateTime.now());
        offlineRewardRepository.save(record);
        
        return Map.of(
            "success", true,
            "lingqi", lingqi,
            "gold", gold,
            "totalLingqi", player.getLingqi(),
            "totalGold", player.getGold()
        );
    }
    
    /**
     * 更新玩家最后在线时间（由外部定时任务调用）
     * 这个方法用于在玩家在线时定期更新lastOnlineTime，以便计算离线收益
     */
    public void updateLastOnlineTime(Long playerId) {
        OfflineReward record = offlineRewardRepository.findByPlayerId(playerId).orElse(null);
        
        if (record == null) {
            record = new OfflineReward();
            record.setPlayerId(playerId);
            record.setAccumulatedLingqi(0);
            record.setAccumulatedGold(0);
            record.setClaimed(false);
        }
        
        record.setLastOnlineTime(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        offlineRewardRepository.save(record);
    }
    
    /**
     * 手动触发离线收益计算（用于测试或手动补偿）
     * 根据指定小时数计算奖励并累积
     */
    public Map<String, Object> calculateReward(Long playerId, int hours) {
        OfflineReward record = offlineRewardRepository.findByPlayerId(playerId).orElse(null);
        
        if (record == null) {
            record = new OfflineReward();
            record.setPlayerId(playerId);
            record.setAccumulatedLingqi(0);
            record.setAccumulatedGold(0);
            record.setClaimed(false);
        }
        
        int maxHours = BASE_MAX_HOURS;
        int cappedHours = Math.min(hours, maxHours);
        
        int lingqi = BASE_LINGQI_PER_HOUR * cappedHours;
        int gold = BASE_GOLD_PER_HOUR * cappedHours;
        
        record.setAccumulatedLingqi(record.getAccumulatedLingqi() + lingqi);
        record.setAccumulatedGold(record.getAccumulatedGold() + gold);
        record.setClaimed(false);
        record.setLastOnlineTime(LocalDateTime.now().minusHours(hours)); // 模拟离线时间
        record.setMaxOfflineHours(maxHours);
        record.setUpdatedAt(LocalDateTime.now());
        
        offlineRewardRepository.save(record);
        
        return Map.of(
            "success", true,
            "message", "离线" + hours + "小时奖励计算完成",
            "addedLingqi", lingqi,
            "addedGold", gold,
            "totalLingqi", record.getAccumulatedLingqi(),
            "totalGold", record.getAccumulatedGold(),
            "maxOfflineHours", maxHours
        );
    }
}