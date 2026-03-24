package com.game.entity;

import jakarta.persistence.*;

/**
 * 赛季实体
 */
@Entity
@Table(name = "t_season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer seasonNum;
    private Long startTime;
    private Long endTime;
    private String status; // active/ended
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getSeasonNum() { return seasonNum; }
    public void setSeasonNum(Integer seasonNum) { this.seasonNum = seasonNum; }
    
    public Long getStartTime() { return startTime; }
    public void setStartTime(Long startTime) { this.startTime = startTime; }
    
    public Long getEndTime() { return endTime; }
    public void setEndTime(Long endTime) { this.endTime = endTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
