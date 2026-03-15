package com.game.controller;

import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/shop")
@CrossOrigin
public class ShopController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private PartnerRepository partnerRepository;
    
    // 商品列表
    private static final List<Map<String, Object>> SHOP_ITEMS = Arrays.asList(
        // 资源类
        Map.of("id", 1, "name", "灵气100", "icon", "💎", "price", 10, "type", "lingqi", "amount", 100),
        Map.of("id", 2, "name", "灵气500", "icon", "💎", "price", 45, "type", "lingqi", "amount", 500),
        Map.of("id", 3, "name", "灵气1000", "icon", "💎", "price", 85, "type", "lingqi", "amount", 1000),
        Map.of("id", 4, "name", "金币500", "icon", "💰", "price", 10, "type", "gold", "amount", 500),
        Map.of("id", 5, "name", "金币2000", "icon", "💰", "price", 35, "type", "gold", "amount", 2000),
        Map.of("id", 6, "name", "金币5000", "icon", "💰", "price", 80, "type", "gold", "amount", 5000),
        
        // 抽卡类
        Map.of("id", 7, "name", "单抽", "icon", "🎫", "price", 50, "type", "gacha", "amount", 1),
        Map.of("id", 8, "name", "十连抽", "icon", "🎫", "price", 450, "type", "gacha", "amount", 10),
        
        // 付费类(模拟)
        Map.of("id", 9, "name", "首充6元", "icon", "🎁", "price", 6, "type", "firstpaid", "reward", Map.of("lingqi", 500, "partner", Map.of("name", "杨戬", "icon", "🐕", "quality", "red", "hp", 1100, "atk", 160))),
        Map.of("id", 10, "name", "月卡30元", "icon", "📅", "price", 30, "type", "monthly", "reward", Map.of("lingqi", 3000))
    );
    
    @GetMapping("/list")
    public Map<String, Object> getShopItems() {
        return Map.of("success", true, "items", SHOP_ITEMS);
    }
    
    @PostMapping("/buy")
    public Map<String, Object> buy(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Integer itemId = (Integer) req.get("itemId");
        
        Player player = playerRepository.findById(playerId).orElse(null);
        if (player == null) return Map.of("success", false, "message", "玩家不存在");
        
        if (itemId < 1 || itemId > SHOP_ITEMS.size()) {
            return Map.of("success", false, "message", "商品不存在");
        }
        
        Map<String, Object> item = SHOP_ITEMS.get(itemId - 1);
        String type = (String) item.get("type");
        int price = (Integer) item.get("price");
        
        // 检查金币是否足够
        if (player.getGold() < price) {
            return Map.of("success", false, "message", "金币不足，需要" + price + "金币");
        }
        
        // 扣除金币
        player.setGold(player.getGold() - price);
        
        // 根据类型发放奖励
        switch (type) {
            case "lingqi":
                int lingqiAmount = (Integer) item.get("amount");
                player.setLingqi(player.getLingqi() + lingqiAmount);
                playerRepository.save(player);
                return Map.of("success", true, "message", "购买成功！获得" + lingqiAmount + "灵气", "lingqi", player.getLingqi(), "gold", player.getGold());
            
            case "gold":
                int goldAmount = (Integer) item.get("amount");
                player.setGold(player.getGold() + goldAmount);
                playerRepository.save(player);
                return Map.of("success", true, "message", "购买成功！获得" + goldAmount + "金币", "gold", player.getGold());
            
            case "gacha":
                // 跳转到抽卡
                return Map.of("success", true, "message", "请使用抽卡功能", "type", "gacha", "gachaType", itemId == 7 ? "single" : "big");
            
            case "firstpaid":
                // 模拟首充(实际应判断是否已首充)
                player.setLingqi(player.getLingqi() + 500);
                
                // 赠送杨戬
                Partner p = new Partner();
                p.setPlayerId(playerId);
                p.setPartnerId(2);
                p.setName("杨戬");
                p.setIcon("🐕");
                p.setQuality("red");
                p.setHp(1100);
                p.setAtk(160);
                p.setSpeed(90);
                p.setLevel(1);
                p.setStar(1);
                p.setGrowthAtk(3);
                p.setGrowthInt(2);
                p.setGrowthLead(2);
                p.setGrowthSpeed(1);
                p.setMaxLevel(80);
                p.setMaxTroops(10000);
                partnerRepository.save(p);
                
                playerRepository.save(player);
                return Map.of("success", true, "message", "购买成功！获得500灵气+杨戬", "lingqi", player.getLingqi());
            
            case "monthly":
                // 发放月卡奖励(30天每天100灵气 = 3000)
                player.setLingqi(player.getLingqi() + 3000);
                playerRepository.save(player);
                return Map.of("success", true, "message", "购买成功！获得3000灵气", "lingqi", player.getLingqi());
            
            default:
                return Map.of("success", false, "message", "未知商品类型");
        }
    }
}
