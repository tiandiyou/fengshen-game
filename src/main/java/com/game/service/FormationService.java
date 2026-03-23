package com.game.service;

import com.game.entity.Formation;
import com.game.mapper.FormationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 阵型系统服务
 * 前后排站位 + 阵型属性加成
 */
@Service
public class FormationService {
    
    @Autowired
    private FormationRepository formationRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 阵型配置
    public static final Map<String, Map<String, Double>> FORMATION_BONUS = Map.of(
        "锋矢阵", Map.of("atk", 1.15, "speed", 1.1),      // 攻击+15%, 速度+10%
        "八卦阵", Map.of("def", 1.15, "dodge", 0.1),      // 防御+15%, 闪避+10%
        "鹤翼阵", Map.of("heal", 1.2, "hp", 1.1),         // 治疗+20%, 生命+10%
        "鱼鳞阵", Map.of("def", 1.1, "hp", 1.15),         // 防御+10%, 生命+15%
        "雁行阵", Map.of("speed", 1.2, "crit", 0.1),      // 速度+20%, 暴击+10%
        "长蛇阵", Map.of("dodge", 0.15, "speed", 1.1)     // 闪避+15%, 速度+10%
    );
    
    // 前后排位置定义
    public static final int FRONT_ROW_COUNT = 3;
    public static final int BACK_ROW_COUNT = 3;
    
    /**
     * 获取玩家所有阵型
     */
    public List<Formation> getPlayerFormations(Long playerId) {
        return formationRepository.findByPlayerId(playerId);
    }
    
    /**
     * 获取玩家默认阵型
     */
    public Formation getDefaultFormation(Long playerId) {
        return formationRepository.findByPlayerIdAndIsDefault(playerId, true)
                .orElse(null);
    }
    
    /**
     * 创建新阵型
     */
    public Formation createFormation(Long playerId, String formationType, 
                                     List<Long> frontRow, List<Long> backRow) {
        
        // 验证阵型类型
        if (!FORMATION_BONUS.containsKey(formationType)) {
            throw new IllegalArgumentException("无效的阵型类型");
        }
        
        // 验证人数
        if (frontRow.size() > FRONT_ROW_COUNT || backRow.size() > BACK_ROW_COUNT) {
            throw new IllegalArgumentException("前后排最多各3人");
        }
        
        // 检查是否设为默认
        boolean isDefault = formationRepository.findByPlayerId(playerId).isEmpty();
        
        Formation formation = new Formation();
        formation.setPlayerId(playerId);
        formation.setFormationType(formationType);
        formation.setFrontRow(toJson(frontRow));
        formation.setBackRow(toJson(backRow));
        formation.setIsDefault(isDefault);
        formation.setCreateTime(System.currentTimeMillis());
        
        return formationRepository.save(formation);
    }
    
    /**
     * 设置默认阵型
     */
    public void setDefaultFormation(Long playerId, Long formationId) {
        // 取消原来的默认
        formationRepository.findByPlayerIdAndIsDefault(playerId, true)
            .ifPresent(f -> {
                f.setIsDefault(false);
                formationRepository.save(f);
            });
        
        // 设置新的默认
        formationRepository.findByPlayerIdAndId(playerId, formationId)
            .ifPresent(f -> {
                f.setIsDefault(true);
                formationRepository.save(f);
            });
    }
    
    /**
     * 更新阵型配置
     */
    public Formation updateFormation(Long playerId, Long formationId,
                                     String formationType, List<Long> frontRow, 
                                     List<Long> backRow) {
        
        Formation formation = formationRepository.findByPlayerIdAndId(playerId, formationId)
                .orElseThrow(() -> new IllegalArgumentException("阵型不存在"));
        
        if (formationType != null && FORMATION_BONUS.containsKey(formationType)) {
            formation.setFormationType(formationType);
        }
        
        if (frontRow != null && frontRow.size() <= FRONT_ROW_COUNT) {
            formation.setFrontRow(toJson(frontRow));
        }
        
        if (backRow != null && backRow.size() <= BACK_ROW_COUNT) {
            formation.setBackRow(toJson(backRow));
        }
        
        return formationRepository.save(formation);
    }
    
    /**
     * 删除阵型
     */
    public void deleteFormation(Long playerId, Long formationId) {
        Formation formation = formationRepository.findByPlayerIdAndId(playerId, formationId)
                .orElseThrow(() -> new IllegalArgumentException("阵型不存在"));
        
        boolean wasDefault = formation.getIsDefault();
        formationRepository.delete(formation);
        
        // 如果删除的是默认阵型，设为其他阵型为默认
        if (wasDefault) {
            formationRepository.findByPlayerId(playerId)
                .stream().findFirst()
                .ifPresent(f -> {
                    f.setIsDefault(true);
                    formationRepository.save(f);
                });
        }
    }
    
    /**
     * 获取阵型加成
     */
    public Map<String, Double> getFormationBonus(String formationType) {
        return FORMATION_BONUS.getOrDefault(formationType, Map.of());
    }
    
    /**
     * 计算前后排加成
     */
    public Map<String, Object> calcPositionBonus(Long playerId, List<Long> teamPartners) {
        Formation formation = getDefaultFormation(playerId);
        Map<String, Object> result = new HashMap<>();
        
        if (formation == null) {
            // 无阵型，默认前排
            result.put("frontRow", teamPartners.subList(0, Math.min(3, teamPartners.size())));
            result.put("backRow", teamPartners.size() > 3 ? teamPartners.subList(3, teamPartners.size()) : Collections.emptyList());
            result.put("frontBonus", Map.of("def", 1.1, "dmgReduction", 0.1));
            result.put("backBonus", Map.of("skillDmg", 1.1, "crit", 0.05));
            return result;
        }
        
        // 解析前后排
        List<Long> front = parseList(formation.getFrontRow());
        List<Long> back = parseList(formation.getBackRow());
        
        // 合并所有使用的武将
        Set<Long> used = new HashSet<>();
        used.addAll(front);
        used.addAll(back);
        
        // 未分配的武将放到前排
        List<Long> unassigned = new ArrayList<>();
        for (Long p : teamPartners) {
            if (!used.contains(p)) {
                unassigned.add(p);
            }
        }
        
        while (unassigned.size() > 0 && front.size() < FRONT_ROW_COUNT) {
            front.add(unassigned.remove(0));
        }
        back.addAll(unassigned);
        
        // 阵型属性加成
        Map<String, Double> formationBonus = getFormationBonus(formation.getFormationType());
        
        // 前排加成
        Map<String, Double> frontBonus = new HashMap<>();
        frontBonus.put("def", 1.1);  // 前排防御+10%
        frontBonus.put("dmgReduction", 0.1);  // 伤害减免10%
        for (Map.Entry<String, Double> e : formationBonus.entrySet()) {
            frontBonus.put(e.getKey(), e.getValue());
        }
        
        // 后排加成
        Map<String, Double> backBonus = new HashMap<>();
        backBonus.put("skillDmg", 1.1);  // 技能伤害+10%
        backBonus.put("crit", 0.05);  // 暴击+5%
        for (Map.Entry<String, Double> e : formationBonus.entrySet()) {
            if (!backBonus.containsKey(e.getKey())) {
                backBonus.put(e.getKey(), e.getValue());
            }
        }
        
        result.put("formationType", formation.getFormationType());
        result.put("frontRow", front);
        result.put("backRow", back);
        result.put("frontBonus", frontBonus);
        result.put("backBonus", backBonus);
        
        return result;
    }
    
    /**
     * 获取所有可用阵型
     */
    public List<Map<String, Object>> getAllFormationTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        for (Map.Entry<String, Map<String, Double>> e : FORMATION_BONUS.entrySet()) {
            Map<String, Object> type = new HashMap<>();
            type.put("name", e.getKey());
            type.put("bonus", e.getValue());
            types.add(type);
        }
        return types;
    }
    
    private String toJson(List<Long> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "[]";
        }
    }
    
    private List<Long> parseList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
