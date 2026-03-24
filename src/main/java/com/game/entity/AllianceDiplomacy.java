package com.game.entity;

import jakarta.persistence.*;

/**
 * 联盟外交关系
 */
@Entity
@Table(name = "t_alliance_diplomacy")
public class AllianceDiplomacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long allianceId1;
    private Long allianceId2;
    private String status; // friendly/hostile/neutral
    private Long updateTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAllianceId1() { return allianceId1; }
    public void setAllianceId1(Long allianceId1) { this.allianceId1 = allianceId1; }
    
    public Long getAllianceId2() { return allianceId2; }
    public void setAllianceId2(Long allianceId2) { this.allianceId2 = allianceId2; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
