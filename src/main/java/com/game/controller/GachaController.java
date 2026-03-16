package com.game.controller;

import com.game.entity.GachaRecord;
import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.GachaRecordRepository;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private static final String TYPE_BIG = "big";      // 大卡包 280灵气
    private static final String TYPE_SMALL = "small";  // 小卡包 100灵气
    private static final String TYPE_PURPLE = "purple"; // 紫将卡包 100灵气
    
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
    
    // 单抽消耗
    private static final int SINGLE_COST = 50;
    
    // 武将数据缓存
    private List<Map<String, Object>> HEROES_DATA;
    
    // 初始化武将数据
    private List<Map<String, Object>> getHeroesData() {
        if (HEROES_DATA == null) {
            try {
                Resource resource = new ClassPathResource("heroes_data.json");
                Scanner scanner = new Scanner(resource.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();
                
                String json = sb.toString();
                // 解析JSON
                json = json.replace("[", "").replace("]", "");
                // 简单解析
                HEROES_DATA = parseHeroes(json);
            } catch (Exception e) {
                System.out.println("加载武将数据失败: " + e.getMessage());
                HEROES_DATA = new ArrayList<>();
            }
        }
        return HEROES_DATA;
    }
    
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseHeroes(String json) {
        List<Map<String, Object>> heroes = new ArrayList<>();
        // 这里简化处理，实际项目中可以使用Jackson
        return heroes;
    }
    
    // 获取所有武将列表(前端展示用)
    @GetMapping("/heroes")
    public Map<String, Object> getAllHeroes() {
        List<Map<String, Object>> heroes = getHeroesData();
        
        // 按阵营分类
        Map<String, List<Map<String, Object>>> byFaction = new HashMap<>();
        for (Map<String, Object> h : heroes) {
            String faction = (String) h.getOrDefault("faction", "阐教");
            byFaction.computeIfAbsent(faction, k -> new ArrayList<>()).add(h);
        }
        
        return Map.of("success", true, "heroes", heroes, "byFaction", byFaction);
    }
    
    @PostMapping("/draw")
    public Map<String, Object> draw(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String type = (String) req.getOrDefault("type", "single");
        Integer times = (Integer) req.getOrDefault("times", 1);
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
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
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            // 概率: 红1%,橙5%,紫24%,蓝70%
            double r = Math.random();
            String quality;
            if (r < 0.01) quality = "red";
            else if (r < 0.06) quality = "orange";
            else if (r < 0.30) quality = "purple";
            else quality = "blue";
            
            // 从GameData获取伙伴数据
            Map<String, Object> pdata = selectPartner(quality);
            
            // 创建伙伴
            Partner partner = createPartner(player.getId(), pdata);
            results.add(buildPartnerInfo(partner, pdata));
            
            saveGachaRecord(player.getId(), "single", quality, (String) pdata.get("name"));
        }
        
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("partners", results);
        result.put("lingqi", player.getLingqi());
        
        return result;
    }
    
    // 多抽
    private Map<String, Object> multiDraw(Player player, String type, int times) {
        if (times != 1 && times != 10) times = 10;
        
        int totalCost = COST.get(type) * times;
        
        if (player.getLingqi() < totalCost) {
            return Map.of("success", false, "message", "灵气不足，需要" + totalCost);
        }
        
        player.setLingqi(player.getLingqi() - totalCost);
        
        double[] rates = QUALITY_RATES.get(type);
        List<Map<String, Object>> results = new ArrayList<>();
        List<String> qualities = new ArrayList<>();
        int orangeCount = 0;
        
        for (int i = 0; i < times; i++) {
            double r = Math.random();
            String quality;
            if (r < rates[0]) quality = "red";
            else if (r < rates[0] + rates[1]) quality = "orange";
            else if (r < rates[0] + rates[1] + rates[2]) quality = "purple";
            else quality = "blue";
            
            if ("orange".equals(quality)) orangeCount++;
            qualities.add(quality);
            
            // 十连保底
            if (i == times - 1 && orangeCount == 0 && times == 10) {
                quality = "orange";
                qualities.set(i, quality);
            }
            
            Map<String, Object> pdata = selectPartner(quality);
            Partner partner = createPartner(player.getId(), pdata);
            results.add(buildPartnerInfo(partner, pdata));
            
            saveGachaRecord(player.getId(), type, quality, (String) pdata.get("name"));
        }
        
        playerRepository.save(player);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("partners", results);
        result.put("qualities", qualities);
        result.put("type", type);
        result.put("times", times);
        result.put("cost", totalCost);
        result.put("lingqi", player.getLingqi());
        
        return result;
    }
    
    // 根据品质选择伙伴
    private Map<String, Object> selectPartner(String quality) {
        // 使用GameData.PARTNERS
        List<Map<String, Object>> candidates = new ArrayList<>();
        
        // 先精确匹配
        for (Map<String, Object> p : com.game.config.GameData.PARTNERS) {
            String q = (String) p.get("quality");
            if (q.equals(quality)) {
                candidates.add(p);
            }
        }
        
        // 如果没有对应品质，向下降级
        if (candidates.isEmpty()) {
            if ("red".equals(quality)) {
                for (Map<String, Object> p : com.game.config.GameData.PARTNERS) {
                    if ("orange".equals(p.get("quality"))) candidates.add(p);
                }
            }
            if (candidates.isEmpty()) {
                for (Map<String, Object> p : com.game.config.GameData.PARTNERS) {
                    if ("purple".equals(p.get("quality"))) candidates.add(p);
                }
            }
            if (candidates.isEmpty()) {
                candidates.addAll(com.game.config.GameData.PARTNERS);
            }
        }
        
        if (candidates.isEmpty()) {
            // 兜底：返回第一个伙伴
            return com.game.config.GameData.PARTNERS.get(0);
        }
        
        return candidates.get((int) (Math.random() * candidates.size()));
    }
    
    // 创建伙伴实体
    @SuppressWarnings("unchecked")
    private Partner createPartner(Long playerId, Map<String, Object> pdata) {
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
        partner.setGrowthAtk(2);
        partner.setGrowthInt(2);
        partner.setGrowthLead(1);
        partner.setGrowthSpeed(1);
        partner.setMaxLevel(80);
        partner.setMaxTroops(10000);
        
        partnerRepository.save(partner);
        return partner;
    }
    
    // 构建伙伴信息(返回给前端)
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildPartnerInfo(Partner partner, Map<String, Object> pdata) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", partner.getId());
        info.put("partnerId", partner.getPartnerId());
        info.put("name", partner.getName());
        info.put("icon", partner.getIcon());
        info.put("quality", partner.getQuality());
        info.put("hp", partner.getHp());
        info.put("atk", partner.getAtk());
        info.put("speed", partner.getSpeed());
        info.put("level", partner.getLevel());
        info.put("star", partner.getStar());
        
        // 阵营信息(如果有)
        if (pdata.containsKey("faction")) {
            info.put("faction", pdata.get("faction"));
        }
        
        // 法术信息(如果有)
        if (pdata.containsKey("magic")) {
            info.put("magic", pdata.get("magic"));
        }
        
        return info;
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
        
        int cost = partner.getLevel() * 10;
        if (player.getGold() < cost) {
            return Map.of("success", false, "message", "金币不足");
        }
        
        player.setGold(player.getGold() - cost);
        partner.setLevel(partner.getLevel() + 1);
        partner.setAtk(partner.getAtk() + partner.getGrowthAtk());
        
        playerRepository.save(player);
        partnerRepository.save(partner);
        
        return Map.of("success", true, "level", partner.getLevel(), "cost", cost);
    }
    
    // 获取伙伴详情(含法术信息)
    @GetMapping("/partner/{id}")
    public Map<String, Object> getPartnerDetail(@PathVariable Long id) {
        Partner partner = partnerRepository.findById(id).orElse(null);
        if (partner == null) {
            return Map.of("success", false, "message", "伙伴不存在");
        }
        
        // 获取伙伴模板数据
        Map<String, Object> pdata = null;
        for (Map<String, Object> p : com.game.config.GameData.PARTNERS) {
            if (p.get("id").equals(partner.getPartnerId())) {
                pdata = p;
                break;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", partner.getId());
        result.put("name", partner.getName());
        result.put("icon", partner.getIcon());
        result.put("quality", partner.getQuality());
        result.put("level", partner.getLevel());
        result.put("star", partner.getStar());
        result.put("hp", partner.getHp());
        result.put("atk", partner.getAtk());
        result.put("speed", partner.getSpeed());
        
        // 模板数据
        if (pdata != null) {
            result.put("faction", pdata.getOrDefault("faction", "阐教"));
            result.put("role", pdata.getOrDefault("role", "输出"));
            result.put("skills", pdata.getOrDefault("skills", new ArrayList<>()));
            if (pdata.containsKey("magic")) {
                result.put("magic", pdata.get("magic"));
            }
        }
        
        return result;
    }
}
