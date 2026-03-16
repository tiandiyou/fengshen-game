package com.game.service;

import com.game.config.FateConfig;
import com.game.entity.Partner;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 缘分计算服务
 */
@Service
public class FateService {
    
    /**
     * 计算队伍缘分加成
     * @param partners 队伍中的伙伴列表
     * @return 缘分加成信息
     */
    public Map<String, Object> calculateFateBonus(List<Partner> partners) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取伙伴名称列表
        List<String> heroNames = new ArrayList<>();
        for (Partner p : partners) {
            if (p.getName() != null) {
                heroNames.add(p.getName());
            }
        }
        
        // 检查所有激活的缘分
        List<Map<String, Object>> activeFates = new ArrayList<>();
        int totalAtkBonus = 0;
        int totalHpBonus = 0;
        int totalDefBonus = 0;
        int totalCritBonus = 0;
        int totalDamageBonus = 0;
        
        for (Map<String, Object> fate : FateConfig.FATES) {
            @SuppressWarnings("unchecked")
            List<String> required = (List<String>) fate.get("heroes");
            
            int matchCount = 0;
            List<String> matchedHeroes = new ArrayList<>();
            for (String hero : required) {
                if (heroNames.contains(hero)) {
                    matchCount++;
                    matchedHeroes.add(hero);
                }
            }
            
            // 至少2人激活缘分
            if (matchCount >= 2) {
                Map<String, Object> activated = new HashMap<>();
                activated.put("id", fate.get("id"));
                activated.put("name", fate.get("name"));
                activated.put("desc", fate.get("desc"));
                activated.put("icon", fate.get("icon"));
                activated.put("matchedHeroes", matchedHeroes);
                activated.put("matchCount", matchCount);
                activated.put("requiredCount", required.size());
                
                FateConfig.FateType type = (FateConfig.FateType) fate.get("type");
                int value = (Integer) fate.get("value");
                activated.put("type", type.name());
                activated.put("value", value);
                
                activeFates.add(activated);
                
                // 累加属性
                switch (type) {
                    case ATK: totalAtkBonus += value; break;
                    case HP: totalHpBonus += value; break;
                    case DEF: totalDefBonus += value; break;
                    case CRIT: totalCritBonus += value; break;
                    case DAMAGE: totalDamageBonus += value; break;
                }
            }
        }
        
        result.put("activeFates", activeFates);
        result.put("totalAtkBonus", totalAtkBonus);
        result.put("totalHpBonus", totalHpBonus);
        result.put("totalDefBonus", totalDefBonus);
        result.put("totalCritBonus", totalCritBonus);
        result.put("totalDamageBonus", totalDamageBonus);
        
        return result;
    }
    
    /**
     * 获取所有缘分配置
     */
    public List<Map<String, Object>> getAllFates() {
        List<Map<String, Object>> fates = new ArrayList<>();
        for (Map<String, Object> fate : FateConfig.FATES) {
            Map<String, Object> f = new HashMap<>(fate);
            @SuppressWarnings("unchecked")
            List<String> heroes = (List<String>) f.get("heroes");
            f.put("heroesCount", heroes.size());
            fates.add(f);
        }
        return fates;
    }
    
    /**
     * 检查某个伙伴可以激活哪些缘分
     */
    public List<Map<String, Object>> getHeroFates(String heroName) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Map<String, Object> fate : FateConfig.FATES) {
            @SuppressWarnings("unchecked")
            List<String> required = (List<String>) fate.get("heroes");
            
            if (required.contains(heroName)) {
                Map<String, Object> f = new HashMap<>();
                f.put("id", fate.get("id"));
                f.put("name", fate.get("name"));
                f.put("desc", fate.get("desc"));
                f.put("icon", fate.get("icon"));
                f.put("type", ((FateConfig.FateType)fate.get("type")).name());
                f.put("value", fate.get("value"));
                f.put("required", required);
                f.put("requiredCount", required.size());
                result.add(f);
            }
        }
        
        return result;
    }
}
