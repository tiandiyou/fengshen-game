package com.game.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 玩家阵型配置
 * 前后排站位 + 阵型加成
 */
@Entity
@Table(name = "formations")
@Data
public class Formation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "player_id")
    private Long playerId;
    
    // 阵型类型: 锋矢阵/八卦阵/鹤翼阵/鱼鳞阵/雁行阵/长蛇阵
    @Column(length = 20)
    private String formationType;
    
    // 前排武将ID列表 (最多3个)
    @Column(length = 100)
    private String frontRow;  // JSON数组: [1,2,3]
    
    // 后排武将ID列表 (最多3个)
    @Column(length = 100)
    private String backRow;   // JSON数组: [4,5,6]
    
    // 是否设为默认阵型
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    // 创建时间
    @Column(name = "create_time")
    private Long createTime = System.currentTimeMillis();
}
