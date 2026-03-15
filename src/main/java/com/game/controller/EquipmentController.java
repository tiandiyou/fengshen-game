package com.game.controller;

import com.game.entity.Equipment;
import com.game.entity.Material;
import com.game.mapper.EquipmentRepository;
import com.game.mapper.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin
public class EquipmentController {
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private MaterialRepository materialRepository;
    
    // 装备数据配置
    private static final List<Map<String, Object>> EQUIPMENT_DATA = Arrays.asList(
        Map.of("id", 1, "name", "火尖枪", "icon", "🔱", "type", "weapon", "quality", "red", "atk", 500, "def", 0, "hp", 0, "speed", 0),
        Map.of("id", 2, "name", "乾坤圈", "icon", "⭕", "type", "accessory", "quality", "red", "atk", 200, "def", 100, "hp", 500, "speed", 50),
        Map.of("id", 3, "name", "风火轮", "icon", "🔥", "type", "accessory", "quality", "red", "atk", 100, "def", 50, "hp", 300, "speed", 200),
        Map.of("id", 4, "name", "混天绫", "icon", "🧣", "type", "armor", "quality", "red", "atk", 0, "def", 400, "hp", 1000, "speed", 0),
        Map.of("id", 5, "name", "打神鞭", "icon", "📿", "type", "weapon", "quality", "orange", "atk", 400, "def", 0, "hp", 0, "speed", 0),
        Map.of("id", 6, "name", "杏黄旗", "icon", "🚩", "type", "armor", "quality", "orange", "atk", 0, "def", 300, "hp", 800, "speed", 0),
        Map.of("id", 7, "name", "阴阳剑", "icon", "⚔️", "type", "weapon", "quality", "purple", "atk", 300, "def", 0, "hp", 0, "speed", 0),
        Map.of("id", 8, "name", "八卦衣", "icon", "👘", "type", "armor", "quality", "purple", "atk", 0, "def", 200, "hp", 500, "speed", 0),
        Map.of("id", 9, "name", "绿辉石", "icon", "💚", "type", "accessory", "quality", "green", "atk", 100, "def", 50, "hp", 200, "speed", 20),
        Map.of("id", 10, "name", "铁剑", "icon", "🗡️", "type", "weapon", "quality", "gray", "atk", 50, "def", 0, "hp", 0, "speed", 0)
    );
    
    // 材料数据
    private static final List<Map<String, Object>> MATERIAL_DATA = Arrays.asList(
        Map.of("type", "iron", "name", "玄铁", "icon", "🪨", "star", 1),
        Map.of("type", "steel", "name", "精钢", "icon", "⚙️", "star", 2),
        Map.of("type", "meteoric", "name", "陨金", "icon", "☄️", "star", 3),
        Map.of("type", "crystal", "name", "天晶", "icon", "💎", "star", 4)
    );
    
    // 合成配方
    private static final Map<String, int[]> RECIPE = Map.of(
        "green", new int[]{4, 0, 0, 0},   // 4玄铁=绿装
        "purple", new int[]{0, 4, 0, 0},   // 4精钢=紫装
        "orange", new int[]{0, 0, 4, 0},   // 4陨金=橙装
        "red", new int[]{0, 0, 0, 4}        // 4天晶=红装
    );
    
    // 获取装备列表
    @GetMapping("/list")
    public Map<String, Object> getEquipmentList(@RequestParam Long playerId) {
        List<Equipment> list = equipmentRepository.findByPlayerId(playerId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("list", list);
        
        int totalZhanli = 0;
        for (Equipment e : list) {
            if (e.getEquipped() != null && e.getEquipped()) {
                totalZhanli += e.getZhanli();
            }
        }
        result.put("zhanli", totalZhanli);
        
        return result;
    }
    
    // 获取材料列表
    @GetMapping("/materials")
    public Map<String, Object> getMaterials(@RequestParam Long playerId) {
        List<Material> list = materialRepository.findByPlayerId(playerId);
        
        // 确保4种材料都存在
        Map<String, Material> materialMap = new HashMap<>();
        for (Material m : list) {
            materialMap.put(m.getType(), m);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> mdata : MATERIAL_DATA) {
            String type = (String) mdata.get("type");
            Material m = materialMap.get(type);
            
            Map<String, Object> item = new HashMap<>();
            item.put("type", type);
            item.put("name", mdata.get("name"));
            item.put("icon", mdata.get("icon"));
            item.put("star", mdata.get("star"));
            item.put("count", m != null ? m.getCount() : 0);
            result.add(item);
        }
        
        return Map.of("success", true, "materials", result);
    }
    
    // 合成装备
    @PostMapping("/craft")
    public Map<String, Object> craft(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String quality = (String) req.get("quality"); // green/purple/orange/red
        
        // 检查材料是否足够
        int[] need = RECIPE.get(quality);
        if (need == null) {
            return Map.of("success", false, "message", "无效品质");
        }
        
        String[] types = {"iron", "steel", "meteoric", "crystal"};
        for (int i = 0; i < 4; i++) {
            Optional<Material> opt = materialRepository.findByPlayerIdAndType(playerId, types[i]);
            int count = opt.map(Material::getCount).orElse(0);
            if (count < need[i]) {
                return Map.of("success", false, "message", "材料不足: 需要" + need[i] + "个" + MATERIAL_DATA.get(i).get("name"));
            }
        }
        
        // 扣除材料
        for (int i = 0; i < 4; i++) {
            if (need[i] > 0) {
                Optional<Material> opt = materialRepository.findByPlayerIdAndType(playerId, types[i]);
                if (opt.isPresent()) {
                    Material m = opt.get();
                    m.setCount(m.getCount() - need[i]);
                    materialRepository.save(m);
                }
            }
        }
        
        // 随机生成一件该品质的装备
        List<Map<String, Object>> available = new ArrayList<>();
        for (Map<String, Object> eq : EQUIPMENT_DATA) {
            if (quality.equals(eq.get("quality"))) {
                available.add(eq);
            }
        }
        
        if (available.isEmpty()) {
            return Map.of("success", false, "message", "该品质装备不存在");
        }
        
        Map<String, Object> eqData = available.get((int) (Math.random() * available.size()));
        
        Equipment eq = new Equipment();
        eq.setPlayerId(playerId);
        eq.setEquipmentId((Integer) eqData.get("id"));
        eq.setName((String) eqData.get("name"));
        eq.setIcon((String) eqData.get("icon"));
        eq.setType((String) eqData.get("type"));
        eq.setQuality(quality);
        eq.setAtk((Integer) eqData.getOrDefault("atk", 0));
        eq.setDef((Integer) eqData.getOrDefault("def", 0));
        eq.setHp((Integer) eqData.getOrDefault("hp", 0));
        eq.setSpeed((Integer) eqData.getOrDefault("speed", 0));
        eq.setEquipped(false);
        eq.setStar(1);
        
        equipmentRepository.save(eq);
        
        return Map.of("success", true, "equipment", eq, "message", "合成成功");
    }
    
    // 穿戴装备
    @PostMapping("/equip")
    public Map<String, Object> equip(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long equipmentId = ((Number) req.get("equipmentId")).longValue();
        Long partnerId = ((Number) req.get("partnerId")).longValue();
        
        Optional<Equipment> opt = equipmentRepository.findByIdAndPlayerId(equipmentId, playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "装备不存在");
        }
        
        Equipment eq = opt.get();
        
        // 先卸下该武将原来的同类型装备
        List<Equipment> oldList = equipmentRepository.findByPartnerId(partnerId);
        for (Equipment old : oldList) {
            if (old.getType().equals(eq.getType())) {
                old.setEquipped(false);
                old.setPartnerId(null);
                equipmentRepository.save(old);
            }
        }
        
        // 穿戴新装备
        eq.setEquipped(true);
        eq.setPartnerId(partnerId);
        equipmentRepository.save(eq);
        
        return Map.of("success", true, "message", "穿戴成功");
    }
    
    // 卸下装备
    @PostMapping("/unequip")
    public Map<String, Object> unequip(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long equipmentId = ((Number) req.get("equipmentId")).longValue();
        
        Optional<Equipment> opt = equipmentRepository.findByIdAndPlayerId(equipmentId, playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "装备不存在");
        }
        
        Equipment eq = opt.get();
        eq.setEquipped(false);
        eq.setPartnerId(null);
        equipmentRepository.save(eq);
        
        return Map.of("success", true, "message", "卸下成功");
    }
    
    // 获得材料(大地图掉落)
    @PostMapping("/gain")
    public Map<String, Object> gainMaterial(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        String type = (String) req.get("type");
        Integer count = (Integer) req.getOrDefault("count", 1);
        
        Optional<Material> opt = materialRepository.findByPlayerIdAndType(playerId, type);
        Material material;
        if (opt.isPresent()) {
            material = opt.get();
            material.setCount(material.getCount() + count);
        } else {
            Map<String, Object> mdata = MATERIAL_DATA.stream()
                .filter(m -> type.equals(m.get("type")))
                .findFirst().orElse(null);
            
            if (mdata == null) {
                return Map.of("success", false, "message", "无效材料类型");
            }
            
            material = new Material();
            material.setPlayerId(playerId);
            material.setType(type);
            material.setName((String) mdata.get("name"));
            material.setIcon((String) mdata.get("icon"));
            material.setStar((Integer) mdata.get("star"));
            material.setCount(count);
        }
        
        materialRepository.save(material);
        
        return Map.of("success", true, "type", type, "count", count);
    }
}
