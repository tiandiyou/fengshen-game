package com.game.controller;

import com.game.entity.Formation;
import com.game.service.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阵型系统API
 */
@RestController
@RequestMapping("/api/formation")
public class FormationController {
    
    @Autowired
    private FormationService formationService;
    
    /**
     * 获取所有可用阵型类型
     */
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getFormationTypes() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "types", formationService.getAllFormationTypes()
        ));
    }
    
    /**
     * 获取玩家的所有阵型
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPlayerFormations(
            @RequestParam Long playerId) {
        List<Formation> formations = formationService.getPlayerFormations(playerId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "formations", formations
        ));
    }
    
    /**
     * 获取玩家默认阵型
     */
    @GetMapping("/default")
    public ResponseEntity<Map<String, Object>> getDefaultFormation(
            @RequestParam Long playerId) {
        Formation formation = formationService.getDefaultFormation(playerId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "formation", formation
        ));
    }
    
    /**
     * 创建新阵型
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createFormation(
            @RequestParam Long playerId,
            @RequestParam String formationType,
            @RequestParam(required = false) List<Long> frontRow,
            @RequestParam(required = false) List<Long> backRow) {
        
        try {
            Formation formation = formationService.createFormation(
                playerId, formationType, 
                frontRow != null ? frontRow : List.of(),
                backRow != null ? backRow : List.of()
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "阵型创建成功");
            result.put("formation", formation);
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * 更新阵型
     */
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateFormation(
            @RequestParam Long playerId,
            @RequestParam Long formationId,
            @RequestParam(required = false) String formationType,
            @RequestParam(required = false) List<Long> frontRow,
            @RequestParam(required = false) List<Long> backRow) {
        
        try {
            Formation formation = formationService.updateFormation(
                playerId, formationId, formationType, frontRow, backRow
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "阵型更新成功");
            result.put("formation", formation);
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * 设置默认阵型
     */
    @PostMapping("/setDefault")
    public ResponseEntity<Map<String, Object>> setDefaultFormation(
            @RequestParam Long playerId,
            @RequestParam Long formationId) {
        
        try {
            formationService.setDefaultFormation(playerId, formationId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "默认阵型设置成功"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * 删除阵型
     */
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteFormation(
            @RequestParam Long playerId,
            @RequestParam Long formationId) {
        
        try {
            formationService.deleteFormation(playerId, formationId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "阵型删除成功"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * 计算位置加成
     */
    @PostMapping("/calc")
    public ResponseEntity<Map<String, Object>> calcPositionBonus(
            @RequestParam Long playerId,
            @RequestParam List<Long> teamPartners) {
        
        Map<String, Object> result = formationService.calcPositionBonus(playerId, teamPartners);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "result", result
        ));
    }
}
