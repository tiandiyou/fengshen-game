package com.game.service;

import com.game.config.BattleBalanceConfig;
import com.game.entity.Formation;
import com.game.entity.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 城战战斗计算服务
 * 核心SLG战斗逻辑
 */
@Service
public class WarBattleService {
    
    @Autowired
    private FormationService formationService;
    
    /**
     * 队伍对战计算
     * @param attackTeam 攻击方队伍
     * @param defendTeam 防守方队伍
     * @param attackerFormationId 攻击方阵型ID (可选)
     * @param defenderFormationId 防守方阵型ID (可选)
     * @return 战斗结果
     */
    public Map<String, Object> battle(List<Partner> attackTeam, List<Partner> defendTeam) {
        return battle(attackTeam, defendTeam, null, null);
    }
    
    /**
     * 队伍对战计算 (带阵型加成)
     */
    public Map<String, Object> battle(List<Partner> attackTeam, List<Partner> defendTeam, 
                                     Long attackerFormationId, Long defenderFormationId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取阵型加成
        Map<String, Double> attackFormationBonus = getFormationBonus(attackerFormationId, attackTeam);
        Map<String, Double> defendFormationBonus = getFormationBonus(defenderFormationId, defendTeam);
        
        // 计算双方总战力 (带阵型加成)
        int attackPower = calcTeamPower(attackTeam, attackFormationBonus);
        int defendPower = calcTeamPower(defendTeam, defendFormationBonus);
        
        result.put("attackPower", attackPower);
        result.put("defendPower", defendPower);
        
        // 胜负判定 (考虑兵种克制)
        double attackBonus = getCounterBonus(attackTeam, defendTeam);
        double defendBonus = getCounterBonus(defendTeam, attackTeam);
        
        double attackScore = attackPower * attackBonus;
        double defendScore = defendPower * defendBonus;
        
        // 战损计算
        int attackLoss = 0;
        int defendLoss = 0;
        String winner;
        
        if (attackScore > defendScore * 1.2) {
            // 大胜
            winner = "attacker";
            defendLoss = (int)(defendPower * 0.8);
            attackLoss = (int)(attackPower * 0.1);
        } else if (attackScore > defendScore) {
            // 险胜
            winner = "attacker";
            defendLoss = (int)(defendPower * 0.5);
            attackLoss = (int)(attackPower * 0.3);
        } else if (defendScore > attackScore * 1.2) {
            // 大胜
            winner = "defender";
            attackLoss = (int)(attackPower * 0.8);
            defendLoss = (int)(defendPower * 0.1);
        } else {
            // 防守方险胜
            winner = "defender";
            attackLoss = (int)(attackPower * 0.5);
            defendLoss = (int)(defendPower * 0.3);
        }
        
        result.put("winner", winner);
        result.put("attackLoss", attackLoss);
        result.put("defendLoss", defendLoss);
        result.put("attackBonus", attackBonus);
        result.put("defendBonus", defendBonus);
        
        // 详细战斗日志
        List<String> logs = new ArrayList<>();
        logs.add("=== 战斗开始 ===");
        logs.add(String.format("攻击方战力: %d (克制加成: %.0f%%)", attackPower, (attackBonus-1)*100));
        logs.add(String.format("防守方战力: %d (克制加成: %.0f%%)", defendPower, (defendBonus-1)*100));
        logs.add("结果: " + ("attacker".equals(winner) ? "攻击方胜利" : "防守方胜利"));
        logs.add(String.format("攻击方损失: %d%% 兵力", attackLoss * 100 / attackPower));
        logs.add(String.format("防守方损失: %d%% 兵力", defendLoss * 100 / defendPower));
        
        result.put("logs", logs);
        
        return result;
    }
    
    /**
     * 计算队伍战力 (无阵型加成)
     */
    public int calcTeamPower(List<Partner> team) {
        return calcTeamPower(team, null);
    }
    
    /**
     * 计算队伍战力 (带阵型加成)
     */
    public int calcTeamPower(List<Partner> team, Map<String, Double> formationBonus) {
        if (team == null || team.isEmpty()) return 0;
        
        int totalAtk = 0;
        int totalHp = 0;
        int totalSpd = 0;
        
        for (Partner p : team) {
            totalAtk += Math.max(p.getAtk(), 0);
            totalHp += Math.max(p.getHp(), 0);
            totalSpd += Math.max(p.getSpeed(), 0);
        }
        
        // 应用阵型加成
        double atkMultiplier = formationBonus != null ? formationBonus.getOrDefault("atk", 1.0) : 1.0;
        double hpMultiplier = formationBonus != null ? formationBonus.getOrDefault("hp", 1.0) : 1.0;
        double spdMultiplier = formationBonus != null ? formationBonus.getOrDefault("speed", 1.0) : 1.0;
        
        totalAtk = (int)(totalAtk * atkMultiplier);
        totalHp = (int)(totalHp * hpMultiplier);
        totalSpd = (int)(totalSpd * spdMultiplier);
        
        // 战力公式: 攻击*2 + 兵力/10 + 速度*1.5
        int power = totalAtk * 2 + totalHp / 10 + totalSpd * 3;
        
        return power;
    }
    
    /**
     * 获取阵型加成
     */
    private Map<String, Double> getFormationBonus(Long formationId, List<Partner> team) {
        if (formationId == null || team == null || team.isEmpty()) {
            return getDefaultFormationBonus(team);
        }
        
        try {
            // 从数据库获取阵型
            Formation formation = formationService.getDefaultFormation(1L); // 简化：使用playerId=1
            if (formation == null) {
                return getDefaultFormationBonus(team);
            }
            
            Map<String, Double> bonus = formationService.getFormationBonus(formation.getFormationType());
            return bonus != null ? bonus : getDefaultFormationBonus(team);
        } catch (Exception e) {
            return getDefaultFormationBonus(team);
        }
    }
    
    /**
     * 获取默认阵型加成 (根据前后排自动分配)
     */
    private Map<String, Double> getDefaultFormationBonus(List<Partner> team) {
        if (team == null || team.size() <= 3) {
            // 小队默认前排加成
            return Map.of("atk", 1.1, "def", 1.1, "hp", 1.05);
        }
        
        // 前排3人 + 后排多人
        Map<String, Double> bonus = new HashMap<>();
        bonus.put("atk", 1.05);    // 全体攻击+5%
        bonus.put("def", 1.1);     // 前排防御+10% (假设前排抗伤害)
        bonus.put("hp", 1.1);      // 前排生命+10%
        bonus.put("speed", 1.05);  // 后排速度+5%
        
        return bonus;
    }
    
    /**
     * 计算实际伤害 (考虑阵型防御/闪避加成)
     */
    public int calcActualDamage(int baseDamage, Map<String, Double> attackerBonus, 
                               Map<String, Double> defenderBonus) {
        double damage = baseDamage;
        
        // 攻击方加成
        if (attackerBonus != null) {
            damage *= attackerBonus.getOrDefault("atk", 1.0);
            double crit = attackerBonus.getOrDefault("crit", 0.0);
            if (Math.random() < crit) {
                damage *= 1.5; // 暴击
            }
        }
        
        // 防守方减免
        if (defenderBonus != null) {
            double def = defenderBonus.getOrDefault("def", 1.0);
            double dmgReduction = defenderBonus.getOrDefault("dmgReduction", 0.0);
            damage /= def;
            damage *= (1.0 - dmgReduction);
            
            // 闪避
            double dodge = defenderBonus.getOrDefault("dodge", 0.0);
            if (Math.random() < dodge) {
                damage = 0; // 闪避
            }
        }
        
        return (int)Math.max(1, damage);
    }
    
    /**
     * 计算兵种克制加成
     */
    public double getCounterBonus(List<Partner> attackTeam, List<Partner> defendTeam) {
        if (attackTeam == null || defendTeam == null) return 1.0;
        
        // 统计攻击方兵种
        Map<String, Integer> attackUnits = new HashMap<>();
        for (Partner p : attackTeam) {
            String unit = p.getType() != null ? p.getType() : "步";
            attackUnits.put(unit, attackUnits.getOrDefault(unit, 0) + 1);
        }
        
        // 统计防守方兵种
        Map<String, Integer> defendUnits = new HashMap<>();
        for (Partner p : defendTeam) {
            String unit = p.getType() != null ? p.getType() : "步";
            defendUnits.put(unit, defendUnits.getOrDefault(unit, 0) + 1);
        }
        
        // 计算克制加成
        double bonus = 1.0;
        
        for (String attackUnit : attackUnits.keySet()) {
            int count = attackUnits.get(attackUnit);
            
            for (String defendUnit : defendUnits.keySet()) {
                double counter = BattleBalanceConfig.getCounterBonus(attackUnit, defendUnit);
                if (counter > 1.0) {
                    // 我克敌，加成 = 克制系数 * 兵种数量占比
                    double ratio = (double)count / attackTeam.size();
                    bonus += (counter - 1.0) * ratio * 0.5;
                }
            }
        }
        
        return Math.min(bonus, 1.5); // 最高50%加成
    }
    
    /**
     * 攻城战斗
     */
    public Map<String, Object> siegeWar(Long cityId, Long attackerId, Long defenderId,
                                       List<Partner> attackTeam, List<Partner> defendTeam) {
        
        Map<String, Object> battleResult = battle(attackTeam, defendTeam);
        
        Map<String, Object> result = new HashMap<>();
        result.put("cityId", cityId);
        result.put("attackerId", attackerId);
        result.put("defenderId", defenderId);
        result.put("battle", battleResult);
        
        String winner = (String) battleResult.get("winner");
        
        if ("attacker".equals(winner)) {
            // 攻击方胜利，占领城池
            result.put("occupy", true);
            result.put("message", "攻城胜利！成功占领城池");
        } else {
            result.put("occupy", false);
            result.put("message", "攻城失败，防守方胜利");
        }
        
        return result;
    }
}
