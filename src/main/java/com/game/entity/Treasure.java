package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_treasure")
public class Treasure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    
    private Integer treasureId;
    
    private String name;
    
    private String icon;
    
    private String bonus;
}
