package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class GameData {
    public static final List<Map<String, Object>> PARTNERS = Arrays.asList(
        Map.of("id", 1, "name", "哪吒", "icon", "🔥", "quality", "red", "hp", 1200, "atk", 180, "speed", 95, "skills", Arrays.asList("火尖枪", "乾坤圈", "风火轮")),
        Map.of("id", 2, "name", "杨戬", "icon", "🐕", "quality", "red", "hp", 1100, "atk", 160, "speed", 90, "skills", Arrays.asList("八九玄功", "哮天犬")),
        Map.of("id", 3, "name", "雷震子", "icon", "⚡", "quality", "purple", "hp", 1000, "atk", 150, "speed", 100, "skills", Arrays.asList("风雷棍")),
        Map.of("id", 4, "name", "姜子牙", "icon", "📜", "quality", "orange", "hp", 900, "atk", 140, "speed", 70, "skills", Arrays.asList("打神鞭", "封神榜")),
        Map.of("id", 5, "name", "金吒", "icon", "🗡️", "quality", "purple", "hp", 950, "atk", 145, "speed", 85, "skills", Arrays.asList("遁龙桩")),
        Map.of("id", 6, "name", "木吒", "icon", "🎋", "quality", "purple", "hp", 930, "atk", 140, "speed", 83, "skills", Arrays.asList("混天绫")),
        Map.of("id", 7, "name", "李靖", "icon", "🏰", "quality", "purple", "hp", 1100, "atk", 120, "speed", 60, "skills", Arrays.asList("玲珑宝塔")),
        Map.of("id", 8, "name", "纣王", "icon", "👑", "quality", "orange", "hp", 1500, "atk", 170, "speed", 65, "skills", Arrays.asList("天子剑")),
        Map.of("id", 9, "name", "妲己", "icon", "🦊", "quality", "orange", "hp", 800, "atk", 200, "speed", 110, "skills", Arrays.asList("魅惑")),
        Map.of("id", 10, "name", "申公豹", "icon", "🐆", "quality", "orange", "hp", 950, "atk", 165, "speed", 88, "skills", Arrays.asList("开天珠")),
        Map.of("id", 11, "name", "赵公明", "icon", "💰", "quality", "orange", "hp", 1300, "atk", 175, "speed", 75, "skills", Arrays.asList("定海珠")),
        Map.of("id", 12, "name", "闻仲", "icon", "👁️", "quality", "orange", "hp", 1400, "atk", 150, "speed", 70, "skills", Arrays.asList("雌雄双鞭"))
    );
    
    public static final List<Map<String, Object>> TREASURES = Arrays.asList(
        Map.of("id", 1, "name", "乾坤圈", "icon", "⭕", "bonus", "暴击+15%", "hp", 200, "atk", 30),
        Map.of("id", 2, "name", "风火轮", "icon", "🔥", "bonus", "速度+20", "hp", 100, "atk", 20),
        Map.of("id", 3, "name", "混天绫", "icon", "🟥", "bonus", "防御+10%", "hp", 150, "atk", 15),
        Map.of("id", 4, "name", "打神鞭", "icon", "📿", "bonus", "伤害+20%", "hp", 120, "atk", 40),
        Map.of("id", 5, "name", "杏黄旗", "icon", "🟡", "bonus", "防御+25%", "hp", 200, "atk", 20),
        Map.of("id", 6, "name", "封神榜", "icon", "📜", "bonus", "全属性+10%", "hp", 300, "atk", 50)
    );
    
    public static final List<Map<String, Object>> CHAPTERS = Arrays.asList(
        Map.of("id", 1, "name", "第一章", "title", "陈塘关", "desc", "哪吒闹海", "enemies", Arrays.asList(
            Map.of("name", "巡海夜叉", "hp", 300, "atk", 30, "icon", "👹"),
            Map.of("name", "敖丙", "hp", 800, "atk", 80, "icon", "🐉")
        ), "reward", Map.of("lingqi", 100, "gold", 50)),
        Map.of("id", 2, "name", "第二章", "title", "渭水河", "desc", "姜子牙下山", "enemies", Arrays.asList(
            Map.of("name", "渔夫", "hp", 400, "atk", 40, "icon", "🎣"),
            Map.of("name", "樵夫", "hp", 450, "atk", 45, "icon", "🪓")
        ), "reward", Map.of("lingqi", 150, "gold", 80)),
        Map.of("id", 3, "name", "第三章", "title", "西岐", "desc", "文王访贤", "enemies", Arrays.asList(
            Map.of("name", "纣使", "hp", 500, "atk", 50, "icon", "👮"),
            Map.of("name", "官兵", "hp", 600, "atk", 60, "icon", "💂")
        ), "reward", Map.of("lingqi", 200, "gold", 100)),
        Map.of("id", 4, "name", "第四章", "title", "佳梦关", "desc", "魔家四将", "enemies", Arrays.asList(
            Map.of("name", "魔礼青", "hp", 800, "atk", 90, "icon", "🌿"),
            Map.of("name", "魔礼红", "hp", 850, "atk", 95, "icon", "🔥")
        ), "reward", Map.of("lingqi", 300, "gold", 150)),
        Map.of("id", 5, "name", "第五章", "title", "青龙关", "desc", "哼哈二将", "enemies", Arrays.asList(
            Map.of("name", "郑伦", "hp", 900, "atk", 100, "icon", "🤨"),
            Map.of("name", "陈奇", "hp", 950, "atk", 105, "icon", "😤")
        ), "reward", Map.of("lingqi", 350, "gold", 180)),
        Map.of("id", 6, "name", "第六章", "title", "汜水关", "desc", "余元余化", "enemies", Arrays.asList(
            Map.of("name", "余化", "hp", 1000, "atk", 120, "icon", "⚔️"),
            Map.of("name", "余元", "hp", 1200, "atk", 130, "icon", "🧙")
        ), "reward", Map.of("lingqi", 400, "gold", 200))
    );
}
