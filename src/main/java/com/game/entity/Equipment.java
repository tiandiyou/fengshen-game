package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private String type; // weapon/armor/helmet/horse
    private String name;
    private Integer quality; // 1-5
    private String special;  // 特技
    private Integer level;
}
