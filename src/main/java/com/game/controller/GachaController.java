package com.game.controller;

import com.game.config.GameData;
import com.game.entity.GachaRecord;
import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.GachaRecordRepository;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/gacha")
@CrossOrigin
public class GachaController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private GachaRecordRepository gachaRecordRepository;
    
    // 卡包类型
    private static final String TYPE_BIG = "big";      // 大卡包 280元宝
    private static final String TYPE_SMALL = "small";  // 小卡包 100元宝
    private static final String TYPE_PURPLE = "purple"; // 紫将卡包 100灵石
    
    // 卡包概率配置
    private static final Map<String, double[]> QUALITY_RATES = Map.of(
        TYPE_BIG, new double[]{0.02, 0.08, 0.25, 0.65},    // 红2%,橙8%,紫25%,蓝65%
        TYPE_SMALL, new double[]{0.0, 0.05, 0.25, 0.70},   // 红0%,橙5%,紫25%,蓝70%
        TYPE_PURPLE, new double[]{0.0, 0.0, 0.35, 0.65}    // 红0%,橙0%,紫35%,蓝65%
    );
    
    // 卡包消耗
    private static final Map<String, Integer> COST = Map.of(
        TYPE_BIG, 280,
        TYPE_SMALL, 100,
        TYPE_PURPLE, 100
    );
    
    // 单抽消耗(旧接口兼容)
    private static final int SINGLE_COST = 50;
    
    @PostMapping("/draw")
    public Map<String, Object> draw(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String type = (String) req.getOrDefault("type", "single"); // single/big/small/purple
        Integer times = (Integer) req.getOrDefault("times", 1); // 抽卡次数
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        // 根据类型处理
        if ("single".equals(type)) {
            return singleDraw(player, 1);
        } else if (TYPE_BIG.equals(type) || TYPE_SMALL.equals(type) || TYPE_PURPLE.equals(type)) {
            return multiDraw(player, type, times);
        } else {
            return Map.of("success", false, "message", "无效卡包类型");
        }
    }
    
    // 单抽
    private Map<String, Object> singleDraw(Player player, int count) {
        if (player.getLingqi() < SINGLE_COST * count) {
            return Map.of("success", false, "message", "灵气不足");
        }
        
        player.setLingqi(player.getLingqi() - SINGLE_COST * count);
        
        List<Map<String, Object>> partners = new ArrayList<>();
        String[] qualities = new String[count];
        
        for (int i = 0; i < count; i++) {
            // 概率: 红1%,橙5%,紫24%,蓝70%
            double r = Math.random();
            String quality;
            if (r < 0.01) quality = "red";
            else if (r < 0.06) quality = "orange";
            else if (r < 0.30) quality = "purple";
            else quality = "blue";
            
            qualities[i] = quality;
            
            // 筛选伙伴
            List<Map<String, Object>> candidates = new ArrayList<>();
            for (Map<String, Object> p : GameData.PARTNERS) {
                if (quality.equals(p.get("quality")) ||
                    ("orange".equals(quality) && "red".equals(p.get("quality")))) {
                    candidates.add(p);
                }
            }
            
            Map<String, Object> pdata = candidates.get((int) (Math.random() * candidates.size()));
            
            // 创建伙伴
            Partner partner = createPartner(player.getId(), pdata, quality);
            partners.add(pdata);
            
            // 记录抽卡
            saveGachaRecord(player.getId(), "single", quality, (String) pdata.get("name"));
        }
        
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("partners", partners);
        result.put("qualities", qualities);
        result.put("lingqi", player.getLingqi());
        
        return result;
    }
    
    // 多抽(大卡包/小卡包/紫将卡包)
    private Map<String, Object> multiDraw(Player player, String type, int times) {
        // 十连抽=10次
        if (times != 1 && times != 10) {
            times = 10; // 默认十连
        }
        
        int totalCost = COST.get(type) * times;
        
        // 检查元宝是否足够(暂时用灵气代替)
        if (player.getLingqi() < totalCost) {
            return Map.of("success", false, "message", "灵气不足，需要" + totalCost);
        }
        
        player.setLingqi(player.getLingqi() - totalCost);
        
        double[] rates = QUALITY_RATES.get(type);
        List<Map<String, Object>> partners = new ArrayList<>();
        List<String> qualities = new ArrayList<>();
        int orangeCount = 0;
        
        for (int i = 0; i < times; i++) {
            // 根据概率获取品质
            double r = Math.random();
            String quality;
            if (r < rates[0]) quality = "red";
            else if (r < rates[0] + rates[1]) quality = "orange";
            else if (r < rates[0] + rates[1] + rates[2]) quality = "purple";
            else quality = "blue";
            
            if ("orange".equals(quality)) orangeCount++;
            qualities.add(quality);
            
            // 十连保底: 至少1个橙将
            if (i == times - 1 && orangeCount == 0 && times == 10) {
                quality = "orange";
                qualities.set(i, quality);
            }
            
            // 筛选伙伴
            List<Map<String, Object>> candidates = new ArrayList<>();
            for (Map<String, Object> p : GameData.PARTNERS) {
                if (quality.equals(p.get("quality")) ||
                    ("orange".equals(quality) && "red".equals(p.get("quality"))) ||
                    "blue".equals(quality)) {
                    candidates.add(p);
                }
            }
            
            Map<String, Object> pdata = candidates.get((int) (Math.random() * candidates.size()));
            
            // 创建伙伴
            Partner partner = createPartner(player.getId(), pdata, quality);
            partners.add(pdata);
            
            // 记录抽卡
            saveGachaRecord(player.getId(), type, quality, (String) pdata.get("name"));
        }
        
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("partners", partners);
        result.put("qualities", qualities);
        result.put("type", type);
        result.put("times", times);
        result.put("cost", totalCost);
        result.put("lingqi", player.getLingqi());
        
        return result;
    }
    
    // 创建伙伴
    private Partner createPartner(Long playerId, Map<String, Object> pdata, String quality) {
        Partner partner = new Partner();
        partner.setPlayerId(playerId);
        partner.setPartnerId((Integer) pdata.get("id"));
        partner.setName((String) pdata.get("name"));
        partner.setIcon((String) pdata.get("icon"));
        partner.setQuality((String) pdata.get("quality"));
        partner.setHp((Integer) pdata.get("hp"));
        partner.setAtk((Integer) pdata.get("atk"));
        partner.setSpeed((Integer) pdata.get("speed"));
        partner.setLevel(1);
        partner.setStar(1);
        
        // 设置成长值
        partner.setGrowthAtk(2);
        partner.setGrowthInt(2);
        partner.setGrowthLead(1);
        partner.setGrowthSpeed(1);
        partner.setMaxLevel(80);
        partner.setMaxTroops(10000);
        
        partnerRepository.save(partner);
        return partner;
    }
    
    // 记录抽卡
    private void saveGachaRecord(Long playerId, String type, String quality, String partnerName) {
        GachaRecord record = new GachaRecord();
        record.setPlayerId(playerId);
        record.setGachaType(type);
        record.setQuality(quality);
        record.setPartnerName(partnerName);
        record.setGachaTime(LocalDateTime.now());
        gachaRecordRepository.save(record);
    }
    
    // 抽卡记录
    @GetMapping("/records")
    public Map<String, Object> records(@RequestParam Long playerId) {
        List<GachaRecord> list = gachaRecordRepository.findByPlayerId(playerId);
        
        // 按时间倒序
        list.sort((a, b) -> b.getGachaTime().compareTo(a.getGachaTime()));
        
        List<Map<String, Object>> records = new ArrayList<>();
        for (GachaRecord r : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", r.getGachaType());
            item.put("quality", r.getQuality());
            item.put("partnerName", r.getPartnerName());
            item.put("time", r.getGachaTime());
            records.add(item);
        }
        
        return Map.of("success", true, "records", records);
    }
    
    // 升级伙伴
    @PostMapping("/upgrade")
    public Map<String, Object> upgrade(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        
        Player player = playerRepository.findById(playerId).orElse(null);
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        
        if (player == null || partner == null) {
            return Map.of("success", false, "message", "数据不存在");
        }
        
        // 升级消耗
        int cost = partner.getLevel() * 10;
        if (player.getGold() < cost) {
            return Map.of("success", false, "message", "金币不足");
        }
        
        player.setGold(player.getGold() - cost);
        partner.setLevel(partner.getLevel() + 1);
        
        // 更新属性
        partner.setAtk(partner.getAtk() + partner.getGrowthAtk());
        partner.setHp(partner.getHp() + 100);
        
        playerRepository.save(player);
        partnerRepository.save(partner);
        
        return Map.of("success", true, "level", partner.getLevel(), "cost", cost);
    }
}
