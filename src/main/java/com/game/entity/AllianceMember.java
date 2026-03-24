package com.game.entity;

import jakarta.persistence.*;

/**
 * 联盟成员
 */
@Entity
@Table(name = "t_alliance_member")
public class AllianceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long allianceId;
    private Long playerId;
    private String role; // leader/officer/member
    private Long joinTime;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAllianceId() { return allianceId; }
    public void setAllianceId(Long allianceId) { this.allianceId = allianceId; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Long getJoinTime() { return joinTime; }
    public void setJoinTime(Long joinTime) { this.joinTime = joinTime; }
}
