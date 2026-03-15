package com.game.service;

import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 抽卡服务
 * 实现5+1模式和6抽保底机制
 */
@Service
public class GachaService {
    
    // 保底阈值
    private static final int GUARANTEE_THRESHOLD = 6;
    
    // 概率配置
    private static final Map<String, Double> QUALITY_RATES = Map.of(
        "red", 0.02,      // 2%
        "orange", 0.05,   // 5%
        "purple", 0.20,   // 20%
        "blue", 0.73      // 73%
    );
    
    /**
     * 单抽
     * @param gachaCount 当前保底计数
     * @return 抽卡结果
     */
    public GachaResult drawSingle(int gachaCount) {
        // 达到保底阈值，强制产出红色
        if (gachaCount >= GUARANTEE_THRESHOLD) {
            return new GachaResult("red", true);
        }
        
        // 随机抽取
        double rand = Math.random();
        double cumulative = 0;
        
        for (Map.Entry<String, Double> entry : QUALITY_RATES.entrySet()) {
            cumulative += entry.getValue();
            if (rand < cumulative) {
                String quality = entry.getKey();
                int newCount = "red".equals(quality) ? 0 : gachaCount + 1;
                return new GachaResult(quality, false, newCount);
            }
        }
        
        // 默认蓝色
        return new GachaResult("blue", false, gachaCount + 1);
    }
    
    /**
     * 十连抽
     * @param gachaCount 当前保底计数
     * @return 抽卡结果列表
     */
    public List<GachaResult> drawTen(int gachaCount) {
        List<GachaResult> results = new ArrayList<>();
        int currentCount = gachaCount;
        
        for (int i = 0; i < 10; i++) {
            // 必出一橙及以上
            if (i == 9) {
                double rand = Math.random();
                if (currentCount >= GUARANTEE_THRESHOLD - 1) {
                    // 保底范围内，必出红
                    results.add(new GachaResult("red", true));
                    currentCount = 0;
                } else if (rand < 0.2) { // 20%概率出橙
                    results.add(new GachaResult("orange", false));
                    currentCount++;
                } else {
                    results.add(new GachaResult("purple", false));
                    currentCount++;
                }
            } else {
                results.add(drawSingle(currentCount));
                currentCount = results.get(i).getNewCount();
            }
        }
        
        return results;
    }
    
    /**
     * 抽卡结果
     */
    public static class GachaResult {
        private final String quality;
        private final boolean isGuarantee;
        private final int newCount;
        
        public GachaResult(String quality, boolean isGuarantee) {
            this(quality, isGuarantee, 0);
        }
        
        public GachaResult(String quality, boolean isGuarantee, int newCount) {
            this.quality = quality;
            this.isGuarantee = isGuarantee;
            this.newCount = newCount;
        }
        
        public String getQuality() { return quality; }
        public boolean isGuarantee() { return isGuarantee; }
        public int getNewCount() { return newCount; }
    }
}
