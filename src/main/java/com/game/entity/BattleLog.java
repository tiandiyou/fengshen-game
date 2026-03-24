package com.game.entity;

import jakarta.persistence.*;

/**
 * 战斗日志实体
 */
@Entity
@Table(name = "t_battle_log")
public class BattleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private Long cityId;
    private String battleType; // siege/pvp/arena
    private Long enemyId;
    private String result; // win/lose/draw
    private Integer attackPower;
    private Integer defendPower;
    private Integer attackLoss;
    private Integer defendLoss;
    private String log;
    private Long battleTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getBattleType() { return battleType; }
    public void setBattleType(String battleType) { this.battleType = battleType; }
    
    public Long getEnemyId() { return enemyId; }
    public void setEnemyId(Long enemyId) { this.enemyId = enemyId; }
    
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    
    public Integer getAttackPower() { return attackPower; }
    public void setAttackPower(Integer attackPower) { this.attackPower = attackPower; }
    
    public Integer getDefendPower() { return defendPower; }
    public void setDefendPower(Integer defendPower) { this.defendPower = defendPower; }
    
    public Integer getAttackLoss() { return attackLoss; }
    public void setAttackLoss(Integer attackLoss) { this.attackLoss = attackLoss; }
    
    public Integer getDefendLoss() { return defendLoss; }
    public void setDefendLoss(Integer defendLoss) { this.defendLoss = defendLoss; }
    
    public String getLog() { return log; }
    public void setLog(String log) { this.log = log; }
    
    public Long getBattleTime() { return battleTime; }
    public void setBattleTime(Long battleTime) { this.battleTime = battleTime; }
}
