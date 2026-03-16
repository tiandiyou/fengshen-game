package com.game.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    private Integer teamIndex; // 队伍序号 (0-4)
    private String name; // 队伍名称
    
    private Long partner1Id; // 上阵伙伴1
    private Long partner2Id; // 上阵伙伴2
    private Long partner3Id; // 上阵伙伴3
    
    private Long cityId; // 所属城池
    
    private Long createTime;
    private Long updateTime;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public Integer getTeamIndex() { return teamIndex; }
    public void setTeamIndex(Integer teamIndex) { this.teamIndex = teamIndex; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getPartner1Id() { return partner1Id; }
    public void setPartner1Id(Long partner1Id) { this.partner1Id = partner1Id; }
    
    public Long getPartner2Id() { return partner2Id; }
    public void setPartner2Id(Long partner2Id) { this.partner2Id = partner2Id; }
    
    public Long getPartner3Id() { return partner3Id; }
    public void setPartner3Id(Long partner3Id) { this.partner3Id = partner3Id; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }
}
