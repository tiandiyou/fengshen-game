package com.game.controller;

import com.game.entity.Mount;
import com.game.mapper.MountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/mount")
@CrossOrigin
public class MountController {
    @Autowired
    private MountRepository mountRepository;
    
    // 坐骑数据配置
    private static final List<Map<String, Object>> MOUNT_DATA = Arrays.asList(
        // 红色坐骑
        Map.of("id", 1, "name", "风火轮", "icon", "🔥", "quality", "red", "atk", 300, "def", 100, "hp", 500, "speed", 500, "skill", "速度+50%"),
        Map.of("id", 2, "name", "金毛犼", "icon", "🦁", "quality", "red", "atk", 400, "def", 200, "hp", 800, "speed", 100, "skill", "攻击+30%"),
        Map.of("id", 3, "name", "青鸾", "icon", "🐦", "quality", "red", "atk", 200, "def", 150, "hp", 600, "speed", 400, "skill", "闪避+20%"),
        // 橙色坐骑
        Map.of("id", 4, "name", "汗血马", "icon", "🐎", "quality", "orange", "atk", 200, "def", 80, "hp", 400, "speed", 300, "skill", "速度+30%"),
        Map.of("id", 5, "name", "赤焰狮", "icon", "🦁", "quality", "orange", "atk", 250, "def", 100, "hp", 500, "speed", 150, "skill", "攻击+20%"),
        Map.of("id", 6, "name", "云鹿", "icon", "🦌", "quality", "orange", "atk", 150, "def", 120, "hp", 600, "speed", 200, "skill", "生命+20%"),
        // 紫色坐骑
        Map.of("id", 7, "name", "黑豹", "icon", "🐆", "quality", "purple", "atk", 150, "def", 60, "hp", 300, "speed", 250, "skill", "速度+20%"),
        Map.of("id", 8, "name", "犀牛", "icon", "🦏", "quality", "purple", "atk", 180, "def", 80, "hp", 400, "speed", 80, "skill", "防御+15%"),
        Map.of("id", 9, "name", "灰狼", "icon", "🐺", "quality", "purple", "atk", 120, "def", 50, "hp", 250, "speed", 180, "skill", "暴击+10%"),
        // 蓝色坐骑
        Map.of("id", 10, "name", "毛驴", "icon", "🫏", "quality", "blue", "atk", 50, "def", 30, "hp", 150, "speed", 80, "skill", "无"),
        Map.of("id", 11, "name", "黄牛", "icon", "🐂", "quality", "blue", "atk", 60, "def", 40, "hp", 200, "speed", 50, "skill", "无"),
        Map.of("id", 12, "name", "山羊", "icon", "🐐", "quality", "blue", "atk", 40, "def", 35, "hp", 180, "speed", 60, "skill", "无")
    );
    
    // 获取坐骑列表
    @GetMapping("/list")
    public Map<String, Object> getMountList(@RequestParam Long playerId) {
        List<Mount> list = mountRepository.findByPlayerId(playerId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("list", list);
        
        int totalZhanli = 0;
        for (Mount m : list) {
            if (m.getEquipped() != null && m.getEquipped()) {
                totalZhanli += m.getZhanli();
            }
        }
        result.put("zhanli", totalZhanli);
        
        return result;
    }
    
    // 遗迹探索(获取坐骑)
    @PostMapping("/explore")
    public Map<String, Object> explore(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        
        // 随机获取一个坐骑
        // 概率: 红1%/橙5%/紫20%/蓝74%
        double rand = Math.random();
        String quality;
        if (rand < 0.01) quality = "red";
        else if (rand < 0.06) quality = "orange";
        else if (rand < 0.26) quality = "purple";
        else quality = "blue";
        
        // 筛选该品质的坐骑
        List<Map<String, Object>> available = new ArrayList<>();
        for (Map<String, Object> m : MOUNT_DATA) {
            if (quality.equals(m.get("quality"))) {
                available.add(m);
            }
        }
        
        Map<String, Object> mdata = available.get((int) (Math.random() * available.size()));
        
        // 创建坐骑
        Mount mount = new Mount();
        mount.setPlayerId(playerId);
        mount.setMountId((Integer) mdata.get("id"));
        mount.setName((String) mdata.get("name"));
        mount.setIcon((String) mdata.get("icon"));
        mount.setQuality(quality);
        mount.setAtk((Integer) mdata.get("atk"));
        mount.setDef((Integer) mdata.get("def"));
        mount.setHp((Integer) mdata.get("hp"));
        mount.setSpeed((Integer) mdata.get("speed"));
        mount.setSkill((String) mdata.get("skill"));
        mount.setEquipped(false);
        mount.setStar(1);
        
        mountRepository.save(mount);
        
        return Map.of("success", true, "mount", mount, "message", "发现" + quality + "坐骑 " + mdata.get("name"));
    }
    
    // 装备坐骑
    @PostMapping("/equip")
    public Map<String, Object> equip(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long mountId = ((Number) req.get("mountId")).longValue();
        
        Optional<Mount> opt = mountRepository.findByIdAndPlayerId(mountId, playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "坐骑不存在");
        }
        
        Mount mount = opt.get();
        mount.setEquipped(true);
        mountRepository.save(mount);
        
        return Map.of("success", true, "message", "装备成功", "zhanli", mount.getZhanli());
    }
    
    // 卸下坐骑
    @PostMapping("/unequip")
    public Map<String, Object> unequip(@RequestBody Map<String, Object> req) {
        Long playerId = ((Number) req.get("playerId")).longValue();
        Long mountId = ((Number) req.get("mountId")).longValue();
        
        Optional<Mount> opt = mountRepository.findByIdAndPlayerId(mountId, playerId);
        if (!opt.isPresent()) {
            return Map.of("success", false, "message", "坐骑不存在");
        }
        
        Mount mount = opt.get();
        mount.setEquipped(false);
        mountRepository.save(mount);
        
        return Map.of("success", true, "message", "卸下成功");
    }
}
