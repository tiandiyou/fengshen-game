package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "t_alliance")
public class Alliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    private Long leaderId;
    private Integer level;
    private Integer memberCount;
    private Long createTime;
}
