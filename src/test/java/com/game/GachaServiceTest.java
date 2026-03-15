package com.game;

import com.game.service.GachaService;
import com.game.service.GachaService.GachaResult;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GachaServiceTest {
    
    private final GachaService gachaService = new GachaService();
    
    @Test
    void testSingleDraw() {
        // 测试单抽
        for (int i = 0; i < 100; i++) {
            GachaResult result = gachaService.drawSingle(0);
            assertNotNull(result.getQuality());
            assertTrue(Arrays.asList("red", "orange", "purple", "blue").contains(result.getQuality()));
        }
    }
    
    @Test
    void testGuaranteeAfter6Orange() {
        // 测试6抽保底
        int count = 5; // 即将保底
        
        GachaResult result = gachaService.drawSingle(count);
        
        // 第6次必定出红
        assertEquals("red", result.getQuality());
        assertTrue(result.isGuarantee());
    }
    
    @Test
    void testGuaranteeCounter() {
        // 测试保底计数器
        int count = 0;
        
        // 连续抽蓝色，保底计数应该增加
        for (int i = 0; i < 5; i++) {
            GachaResult result = gachaService.drawSingle(count);
            count = result.getNewCount();
            if (result.getQuality().equals("blue") || result.getQuality().equals("purple")) {
                assertEquals(i + 1, count);
            }
        }
        
        // 第6抽应该保底
        GachaResult result = gachaService.drawSingle(count);
        assertEquals("red", result.getQuality());
    }
    
    @Test
    void testTenDraw() {
        // 测试十连抽
        List<GachaResult> results = gachaService.drawTen(0);
        
        assertEquals(10, results.size());
        
        // 最后一抽应该是橙色或红色
        GachaResult lastResult = results.get(9);
        assertTrue(Arrays.asList("red", "orange", "purple").contains(lastResult.getQuality()));
    }
    
    @Test
    void testTenDrawGuarantee() {
        // 测试十连抽保底
        List<GachaResult> results = gachaService.drawTen(5); // 即将保底
        
        // 应该包含至少一个红色
        boolean hasRed = results.stream().anyMatch(r -> "red".equals(r.getQuality()));
        assertTrue(hasRed, "十连抽应该包含红色武将");
    }
    
    @Test
    void testProbabilityDistribution() {
        // 测试概率分布(大数据量)
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("red", 0);
        countMap.put("orange", 0);
        countMap.put("purple", 0);
        countMap.put("blue", 0);
        
        int total = 10000;
        for (int i = 0; i < total; i++) {
            GachaResult result = gachaService.drawSingle(0);
            countMap.put(result.getQuality(), countMap.get(result.getQuality()) + 1);
        }
        
        // 验证概率范围(允许一定误差)
        double redRate = (double) countMap.get("red") / total;
        double orangeRate = (double) countMap.get("orange") / total;
        
        System.out.println("Red rate: " + redRate);
        System.out.println("Orange rate: " + orangeRate);
        
        assertTrue(redRate > 0.01 && redRate < 0.04, "红色概率应该在1%-4%之间");
        assertTrue(orangeRate > 0.03 && orangeRate < 0.08, "橙色概率应该在3%-8%之间");
    }
}
