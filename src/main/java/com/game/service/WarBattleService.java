package com.game.service;

import com.game.config.BattleBalanceConfig;
import com.game.entity.Partner;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 城战战斗计算服务
 * 核心SLG战斗逻辑
 */
@Service
public class WarBattleService {
    
    /**
     * 队伍对战计算
     * @param attackTeam 攻击方队伍
     * @param defendTeam 防守方队伍
     * @return 战斗结果
     */
    public Map<String, Object> battle(List<Partner> attackTeam, List<Partner> defendTeam) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算双方总战力
        int attackPower = calcTeamPower(attackTeam);
        int defendPower = calcTeamPower(defendTeam);
        
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
     * 计算队伍战力
     */
    public int calcTeamPower(List<Partner> team) {
        if (team == null || team.isEmpty()) return 0;
        
        int totalAtk = 0;
        int totalHp = 0;
        int totalSpd = 0;
        
        for (Partner p : team) {
            totalAtk +=  Math.max(p.getAtk(), 0);
            totalHp +=  Math.max(p.getHp(), 0);
            totalSpd +=  Math.max(p.getSpeed(), 0);
        }
        
        // 战力公式: 攻击*2 + 兵力/10 + 速度*1.5
        int power = totalAtk * 2 + totalHp / 10 + totalSpd * 3;
        
        return power;
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
