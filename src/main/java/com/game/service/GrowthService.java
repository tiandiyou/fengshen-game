package com.game.service;

import com.game.config.GrowthConfig;
import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 武将成长服务
 */
@Service
public class GrowthService {
    
    @Autowired
    private PartnerRepository partnerRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    /**
     * 升级
     */
    public Map<String, Object> levelUp(Long playerId, Long partnerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        // 检查是否满级
        if (partner.getLevel() >= partner.getMaxLevel()) {
            return Map.of("success", false, "message", "已达最大等级");
        }
        
        // 计算升级消耗
        int cost = partner.getLevel() * GrowthConfig.LEVEL_UP_COST.get("lingqi");
        
        if (player.getLingqi() < cost) {
            return Map.of("success", false, "message", "灵气不足，需要" + cost);
        }
        
        // 扣费并升级
        player.setLingqi(player.getLingqi() - cost);
        playerRepository.save(player);
        
        partner.setLevel(partner.getLevel() + 1);
        
        // 属性成长
        partner.setAtk(partner.getAtk() + GrowthConfig.LEVEL_UP_BONUS.get("atk"));
        partner.setHp(partner.getHp() + GrowthConfig.LEVEL_UP_BONUS.get("hp"));
        partner.setSpeed(partner.getSpeed() + GrowthConfig.LEVEL_UP_BONUS.get("speed"));
        
        partnerRepository.save(partner);
        
        return Map.of(
            "success", true,
            "message", "升级成功",
            "newLevel", partner.getLevel(),
            "cost", cost,
            "newAtk", partner.getAtk(),
            "newHp", partner.getHp()
        );
    }
    
    /**
     * 升星
     */
    public Map<String, Object> starUp(Long playerId, Long partnerId, Long consumePartnerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        int currentStar = partner.getStar();
        
        // 检查是否满星
        if (currentStar >= 10) {
            return Map.of("success", false, "message", "已达最大星级");
        }
        
        // 检查消耗的伙伴
        Partner consumePartner = partnerRepository.findById(consumePartnerId).orElse(null);
        if (consumePartner == null || !consumePartner.getName().equals(partner.getName())) {
            return Map.of("success", false, "message", "消耗的伙伴必须是同名武将");
        }
        
        // 检查同名卡数量
        List<Partner> sameNamePartners = partnerRepository.findAll().stream()
            .filter(p -> p.getPlayerId().equals(playerId) && p.getName().equals(partner.getName()))
            .toList();
        
        int needCount = GrowthConfig.STAR_UP_COST.get(currentStar + 1);
        if (sameNamePartners.size() < needCount + 1) {
            return Map.of("success", false, "message", "同名卡不足，需要" + needCount + "张");
        }
        
        // 删除消耗的伙伴
        partnerRepository.delete(consumePartner);
        
        // 升星
        partner.setStar(currentStar + 1);
        partnerRepository.save(partner);
        
        // 属性加成
        int bonusPercent = GrowthConfig.STAR_BONUS.get(currentStar + 1);
        
        return Map.of(
            "success", true,
            "message", "升星成功",
            "newStar", partner.getStar(),
            "bonusPercent", bonusPercent
        );
    }
    
    /**
     * 突破
     */
    public Map<String, Object> breakthrough(Long playerId, Long partnerId) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        String quality = partner.getQuality();
        
        // 检查是否可以突破
        if ("blue".equals(quality)) {
            return Map.of("success", false, "message", "蓝色品质无法突破");
        }
        
        // 获取突破配置
        Map<String, Object> config = GrowthConfig.BREAKTHROUGH.get(quality);
        if (config == null) {
            return Map.of("success", false, "message", "突破配置错误");
        }
        
        int cost = (Integer) config.get("cost");
        if (player.getLingqi() < cost) {
            return Map.of("success", false, "message", "灵气不足，需要" + cost);
        }
        
        // 扣费
        player.setLingqi(player.getLingqi() - cost);
        playerRepository.save(player);
        
        // 突破（降一级品质）
        String newQuality = getLowerQuality(quality);
        partner.setQuality(newQuality);
        partnerRepository.save(partner);
        
        // 获取新的突破配置（为下次准备）
        Map<String, Object> nextConfig = GrowthConfig.BREAKTHROUGH.get(newQuality);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "突破成功");
        result.put("newQuality", newQuality);
        result.put("cost", cost);
        
        if (nextConfig != null) {
            result.put("nextCost", nextConfig.get("cost"));
        } else {
            result.put("nextCost", "已达最高");
        }
        
        return result;
    }
    
    private String getLowerQuality(String quality) {
        return switch (quality) {
            case "red" -> "orange";
            case "orange" -> "purple";
            case "purple" -> "blue";
            default -> "blue";
        };
    }
    
    /**
     * 获取武将成长信息
     */
    public Map<String, Object> getGrowthInfo(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        if (partner == null) {
            return Map.of("success", false, "message", "伙伴不存在");
        }
        
        Map<String, Object> info = new HashMap<>();
        info.put("success", true);
        info.put("partnerId", partner.getId());
        info.put("name", partner.getName());
        info.put("level", partner.getLevel());
        info.put("maxLevel", partner.getMaxLevel());
        info.put("star", partner.getStar());
        info.put("quality", partner.getQuality());
        
        // 升级信息
        int nextLevelCost = partner.getLevel() * GrowthConfig.LEVEL_UP_COST.get("lingqi");
        info.put("nextLevelCost", nextLevelCost);
        
        // 升星信息
        int nextStar = partner.getStar() + 1;
        if (nextStar <= 10) {
            info.put("nextStarCost", GrowthConfig.STAR_UP_COST.get(nextStar));
            info.put("nextStarBonus", GrowthConfig.STAR_BONUS.get(nextStar));
        }
        
        // 突破信息
        String quality = partner.getQuality();
        if (!"blue".equals(quality)) {
            Map<String, Object> btConfig = GrowthConfig.BREAKTHROUGH.get(quality);
            if (btConfig != null) {
                info.put("breakthroughCost", btConfig.get("cost"));
            }
        }
        
        // 战力计算
        int power = calculatePower(partner);
        info.put("power", power);
        
        return info;
    }
    
    /**
     * 计算战力
     */
    public int calculatePower(Partner partner) {
        // 基础战力
        int basePower = partner.getAtk() * 2 + partner.getHp() / 10;
        
        // 星级加成
        int starBonus = GrowthConfig.STAR_BONUS.getOrDefault(partner.getStar(), 0);
        basePower = basePower * (100 + starBonus) / 100;
        
        return basePower;
    }
}
