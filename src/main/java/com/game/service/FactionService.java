package com.game.service;

import com.game.config.FactionBonus;
import com.game.entity.Partner;
import com.game.mapper.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 阵营加成计算服务
 */
@Service
public class FactionService {
    
    @Autowired
    private PartnerRepository partnerRepository;
    
    /**
     * 计算队伍阵营加成
     * @param partners 队伍中的伙伴列表
     * @return 加成后的属性Map
     */
    public Map<String, Integer> calculateFactionBonus(List<Partner> partners) {
        Map<String, Integer> bonus = new HashMap<>();
        bonus.put("atkBonus", 0);
        bonus.put("hpBonus", 0);
        bonus.put("defBonus", 0);
        bonus.put("speedBonus", 0);
        bonus.put("critBonus", 0);
        
        if (partners == null || partners.isEmpty()) {
            return bonus;
        }
        
        // 统计各阵营数量
        Map<String, Integer> factionCount = new HashMap<>();
        for (Partner p : partners) {
            String faction = p.getFaction();
            if (faction != null && !faction.isEmpty()) {
                factionCount.put(faction, factionCount.getOrDefault(faction, 0) + 1);
            }
        }
        
        // 计算每个阵营的加成
        for (Map.Entry<String, Integer> entry : factionCount.entrySet()) {
            String faction = entry.getKey();
            int count = entry.getValue();
            
            if (count < 1) continue;
            
            FactionBonus.BonusType bonusType = FactionBonus.FACTION_BONUS_TYPE.get(faction);
            if (bonusType == null) continue;
            
            // 基础加成：阵营中每有1人全体+5%
            int baseBonus = count * FactionBonus.FACTION_BONUS_BASE;
            
            // 高级加成：阵营>=3人时，额外+10%
            if (count >= 3) {
                baseBonus += FactionBonus.FACTION_BONUS_ADVANCED;
            }
            
            // 应用加成
            switch (bonusType) {
                case ATK:
                    bonus.put("atkBonus", bonus.get("atkBonus") + baseBonus);
                    break;
                case HP:
                    bonus.put("hpBonus", bonus.get("hpBonus") + baseBonus);
                    break;
                case DEF:
                    bonus.put("defBonus", bonus.get("defBonus") + baseBonus);
                    break;
                case SPEED:
                    bonus.put("speedBonus", bonus.get("speedBonus") + baseBonus);
                    break;
                case CRIT:
                    bonus.put("critBonus", bonus.get("critBonus") + baseBonus);
                    break;
                case ALL:
                    bonus.put("atkBonus", bonus.get("atkBonus") + baseBonus);
                    bonus.put("hpBonus", bonus.get("hpBonus") + baseBonus);
                    bonus.put("defBonus", bonus.get("defBonus") + baseBonus);
                    break;
            }
        }
        
        return bonus;
    }
    
    /**
     * 计算单个伙伴的最终属性（含阵营加成）
     */
    public Map<String, Object> getPartnerFinalStats(Partner partner, List<Partner> team) {
        Map<String, Object> stats = new HashMap<>();
        
        // 基础属性
        stats.put("hp", partner.getHp());
        stats.put("atk", partner.getAtk());
        stats.put("speed", partner.getSpeed());
        
        // 计算阵营加成
        Map<String, Integer> bonus = calculateFactionBonus(team);
        
        // 应用加成
        int hpBonus = bonus.getOrDefault("hpBonus", 0);
        int atkBonus = bonus.getOrDefault("atkBonus", 0);
        
        stats.put("hpBonus", hpBonus);
        stats.put("atkBonus", atkBonus);
        stats.put("finalHp", partner.getHp() * (100 + hpBonus) / 100);
        stats.put("finalAtk", partner.getAtk() * (100 + atkBonus) / 100);
        
        return stats;
    }
    
    /**
     * 获取阵营信息
     */
    public Map<String, Object> getFactionInfo(String faction) {
        Map<String, Object> info = new HashMap<>();
        info.put("name", faction);
        
        FactionBonus.BonusType bonusType = FactionBonus.FACTION_BONUS_TYPE.get(faction);
        info.put("bonusType", bonusType != null ? bonusType.name() : "NONE");
        
        String desc = "";
        if (bonusType != null) {
            switch (bonusType) {
                case ATK: desc = "全队攻击+" + FactionBonus.FACTION_BONUS_BASE + "%/人"; break;
                case HP: desc = "全队生命+" + FactionBonus.FACTION_BONUS_BASE + "%/人"; break;
                case DEF: desc = "全队防御+" + FactionBonus.FACTION_BONUS_BASE + "%/人"; break;
                case SPEED: desc = "全队速度+" + FactionBonus.FACTION_BONUS_BASE + "%/人"; break;
                case CRIT: desc = "全队暴击+" + FactionBonus.FACTION_BONUS_BASE + "%/人"; break;
            }
            if (FactionBonus.FACTION_BONUS_ADVANCED > 0) {
                desc += "，3人以上额外+" + FactionBonus.FACTION_BONUS_ADVANCED + "%";
            }
        }
        info.put("description", desc);
        
        return info;
    }
}
