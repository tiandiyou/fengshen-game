package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private Long cityId;
    private String type;      // palace/warehouse/barracks/market/farm/wood/iron
    private Integer level;
    private Long updateTime;
}
