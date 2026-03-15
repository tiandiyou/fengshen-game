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
    
    private static final List<Map<String, Object>> SHOP_ITEMS = Arrays.asList(
        Map.of("id", 1, "name", "灵气100", "icon", "💎", "price", 10, "type", "lingqi", "amount", 100),
        Map.of("id", 2, "name", "金币500", "icon", "💰", "price", 10, "type", "gold", "amount", 500),
        Map.of("id", 3, "name", "单抽", "icon", "🎫", "price", 50, "type", "gacha", "amount", 1),
        Map.of("id", 4, "name", "十连抽", "icon", "🎫", "price", 450, "type", "gacha", "amount", 10),
        Map.of("id", 5, "name", "首充6元", "icon", "🎁", "price", 6, "type", "firstpaid", "reward", Map.of("lingqi", 500, "partner", Map.of("name", "杨戬", "icon", "🐕", "quality", "red", "hp", 1100, "atk", 160))),
        Map.of("id", 6, "name", "月卡30元", "icon", "📅", "price", 30, "type", "monthly", "reward", Map.of("lingqi", 3000))
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
        
        Map<String, Object> item = SHOP_ITEMS.get(itemId - 1);
        String type = (String) item.get("type");
        
        // 这里应该对接真实支付渠道，简化处理
        if ("firstpaid".equals(type)) {
            if (player.getLingqi() != null && player.getLingqi() > 10000) {
                // 模拟首充判断
                return Map.of("success", false, "message", "请使用真实支付");
            }
            // 模拟发放首充奖励
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
            p.setStar(0);
            p.setSkills("八九玄功,哮天犬");
            partnerRepository.save(p);
            
            playerRepository.save(player);
            return Map.of("success", true, "message", "购买成功！获得500灵气+杨戬");
        }
        
        if ("monthly".equals(type)) {
            player.setLingqi(player.getLingqi() + 3000);
            playerRepository.save(player);
            return Map.of("success", true, "message", "购买成功！获得3000灵气");
        }
        
        return Map.of("success", false, "message", "此功能需要真实支付");
    }
}
