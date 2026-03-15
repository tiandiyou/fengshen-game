package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_partner")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long playerId;
    
    private Integer partnerId;
    
    private String name;
    
    private String icon;
    
    private String quality;
    
    private Integer hp;
    
    private Integer atk;
    
    private Integer speed;
    
    private Integer level = 1;
    
    private Integer star = 0;
    
    private String skills;
    
    private Boolean selected = false;
}
