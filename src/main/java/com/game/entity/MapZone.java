package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 地图区域实体
 */
@Data
@Entity
@Table(name = "t_map_zone")
public class MapZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 区域ID
    private Integer zoneId;
    
    // 区域名称
    private String name;
    
    // 区域类型: birth(出生州)/resource(资源州)/capital(朝歌)
    private String type;
    
    // 等级要求
    private Integer levelReq;
    
    // 资源产出
    private Integer goldOutput;
    private Integer lingqiOutput;
    
    // 遗迹类型
    private String relicType;
    
    // 坐标
    private Integer x;
    private Integer y;
}
