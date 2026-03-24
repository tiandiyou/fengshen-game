package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 抽卡记录
 * 用于记录抽卡历史和保底计数
 */
@Data
@Entity
@Table(name = "t_gacha_record")
public class GachaRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 玩家ID
    private Long playerId;
    
    // 抽卡时间
    private LocalDateTime gachaTime;
    
    // 抽到的武将ID
    private Integer partnerId;
    
    // 武将名称
    private String partnerName;
    
    // 武将品质: red/orange/purple/blue
    private String quality;
    
    // 抽卡类型: single(单抽)/ten(十连)
    private String gachaType;
    
    // 是否为保底产出
    private Boolean isGuarantee = false;
    
    // ========== Getters and Setters ==========
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    
    public LocalDateTime getGachaTime() { return gachaTime; }
    public void setGachaTime(LocalDateTime gachaTime) { this.gachaTime = gachaTime; }
    
    public Integer getPartnerId() { return partnerId; }
    public void setPartnerId(Integer partnerId) { this.partnerId = partnerId; }
    
    public String getPartnerName() { return partnerName; }
    public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
    
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    
    public String getGachaType() { return gachaType; }
    public void setGachaType(String gachaType) { this.gachaType = gachaType; }
    
    public Boolean getIsGuarantee() { return isGuarantee; }
    public void setIsGuarantee(Boolean isGuarantee) { this.isGuarantee = isGuarantee; }
    
    @PrePersist
    public void prePersist() {
        this.gachaTime = LocalDateTime.now();
    }
}
