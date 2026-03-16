package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
}
