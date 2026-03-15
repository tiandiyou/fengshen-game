package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "t_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private Integer lingqi = 200;
    
    private Integer gold = 100;
    
    private Integer zhanli = 0;
    
    private Integer chapterId = 0;
    
    private Integer battleCount = 0;
    
    private Integer signinDays = 0;
    
    private String signinLastDate = "";
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private List<Partner> partners;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private List<Treasure> treasures;
}
