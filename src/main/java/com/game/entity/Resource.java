package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private Long cityId;
    private Integer food;      // 粮食
    private Integer wood;      // 木材
    private Integer iron;     // 铁矿
    private Integer gold;     // 金币
    private Long updateTime;
}
