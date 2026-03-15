package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 武将数据配置 - 简化版
 */
@Component
public class PartnerData {
    
    // 红将ID列表
    public static final List<Integer> RED_IDS = Arrays.asList(101, 102, 103, 104, 105);
    
    // 橙将ID列表
    public static final List<Integer> ORANGE_IDS = Arrays.asList(201, 202, 203, 204, 205);
    
    // 获取武将基础属性
    public static Map<String, Object> getPartnerBaseStats(int partnerId) {
        Map<String, Object> stats = new HashMap<>();
        
        switch(partnerId) {
            case 101: // 哪吒
                stats.put("name", "哪吒");
                stats.put("quality", "red");
                stats.put("type", "骑");
                stats.put("atk", 180);
                stats.put("intelligence", 85);
                stats.put("lead", 90);
                stats.put("speed", 95);
                break;
            case 102: // 杨戬
                stats.put("name", "杨戬");
                stats.put("quality", "red");
                stats.put("type", "步");
                stats.put("atk", 160);
                stats.put("intelligence", 80);
                stats.put("lead", 85);
                stats.put("speed", 90);
                break;
            case 103: // 姜子牙
                stats.put("name", "姜子牙");
                stats.put("quality", "red");
                stats.put("type", "策");
                stats.put("atk", 60);
                stats.put("intelligence", 140);
                stats.put("lead", 95);
                stats.put("speed", 70);
                break;
            case 201: // 金吒
                stats.put("name", "金吒");
                stats.put("quality", "orange");
                stats.put("type", "骑");
                stats.put("atk", 130);
                stats.put("intelligence", 45);
                stats.put("lead", 68);
                stats.put("speed", 78);
                break;
            default:
                stats.put("name", "武将" + partnerId);
                stats.put("quality", "blue");
                stats.put("type", "步");
                stats.put("atk", 50);
                stats.put("intelligence", 30);
                stats.put("lead", 40);
                stats.put("speed", 30);
        }
        return stats;
    }
}
