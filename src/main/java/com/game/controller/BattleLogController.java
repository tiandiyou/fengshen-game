package com.game.controller;

import com.game.entity.BattleLog;
import com.game.mapper.BattleLogRepository;
import com.game.service.WarBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/battle")
@CrossOrigin
public class BattleLogController {
    
    @Autowired
    private BattleLogRepository logRepo;
    
    @Autowired
    private WarBattleService warService;
    
    // 记录战斗
    @PostMapping("/record")
    public Map<String, Object> record(
            Long playerId, Long cityId, String battleType, Long enemyId,
            String result, Integer attackPower, Integer defendPower,
            Integer attackLoss, Integer defendLoss, String log) {
        
        BattleLog b = new BattleLog();
        b.setPlayerId(playerId);
        b.setCityId(cityId);
        b.setBattleType(battleType);
        b.setEnemyId(enemyId);
        b.setResult(result);
        b.setAttackPower(attackPower);
        b.setDefendPower(defendPower);
        b.setAttackLoss(attackLoss);
        b.setDefendLoss(defendLoss);
        b.setLog(log);
        b.setBattleTime(System.currentTimeMillis());
        
        logRepo.save(b);
        
        return Map.of("success", true, "message", "战斗记录已保存");
    }
    
    // 战斗记录列表
    @GetMapping("/log")
    public Map<String, Object> log(Long playerId, Integer page, Integer size) {
        if (page == null) page = 0;
        if (size == null) size = 10;
        
        List<BattleLog> logs = logRepo.findByPlayerIdOrderByBattleTimeDesc(playerId);
        
        // 简单分页
        int from = page * size;
        int to = Math.min(from + size, logs.size());
        List<BattleLog> pageLogs = from < logs.size() ? logs.subList(from, to) : Collections.emptyList();
        
        return Map.of("success", true, "logs", pageLogs, "total", logs.size());
    }
    
    // 统计
    @GetMapping("/stats")
    public Map<String, Object> stats(Long playerId) {
        List<BattleLog> logs = logRepo.findByPlayerIdOrderByBattleTimeDesc(playerId);
        
        int wins = 0, losses = 0, draws = 0;
        int totalAttackPower = 0, totalDefendPower = 0;
        
        for (BattleLog b : logs) {
            switch (b.getResult()) {
                case "win" -> wins++;
                case "lose" -> losses++;
                case "draw" -> draws++;
            }
            totalAttackPower += b.getAttackPower();
            totalDefendPower += b.getDefendPower();
        }
        
        return Map.of(
            "success", true,
            "total", logs.size(),
            "wins", wins,
            "losses", losses,
            "draws", draws,
            "winRate", logs.isEmpty() ? 0 : (wins * 100 / logs.size()) + "%",
            "avgAttackPower", logs.isEmpty() ? 0 : totalAttackPower / logs.size(),
            "avgDefendPower", logs.isEmpty() ? 0 : totalDefendPower / logs.size()
        );
    }
}
