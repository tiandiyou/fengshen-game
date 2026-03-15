package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 联盟成员实体
 */
@Data
@Entity
@Table(name = "t_alliance_member")
public class AllianceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 联盟ID
    private Long allianceId;
    
    // 玩家ID
    private Long playerId;
    
    // 玩家名称
    private String playerName;
    
    // 职位: leader/officer/member
    private String role;
    
    // 贡献
    private Integer contribution;
    
    // 加入时间
    private Long joinTime;
}
