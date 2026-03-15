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
    
    // 战法数据
    public static final List<Map<String, Object>> SKILLS = Arrays.asList(
        // 主动战法
        Map.of("id", 1, "name", "火尖枪", "icon", "🔱", "type", "主动", "effectType", "伤害", "effectValue", 150, "cooldown", 2, "cost", 30, "targetType", "单体", "description", "火尖枪攻击单个目标，造成150%武力伤害"),
        Map.of("id", 2, "name", "八九玄功", "icon", "🌀", "type", "主动", "effectType", "伤害", "effectValue", 140, "cooldown", 2, "cost", 25, "targetType", "单体", "description", "杨戬绝技，造成140%武力伤害"),
        Map.of("id", 3, "name", "风雷棍", "icon", "⚡", "type", "主动", "effectType", "伤害", "effectValue", 130, "cooldown", 2, "cost", 20, "targetType", "单体", "description", "雷震子绝技，造成130%武力伤害"),
        Map.of("id", 4, "name", "打神鞭", "icon", "📿", "type", "主动", "effectType", "伤害", "effectValue", 145, "cooldown", 2, "cost", 28, "targetType", "单体", "description", "姜子牙法宝，造成145%智力伤害"),
        Map.of("id", 5, "name", "天子剑", "icon", "👑", "type", "主动", "effectType", "伤害", "effectValue", 160, "cooldown", 3, "cost", 35, "targetType", "单体", "description", "纣王佩剑，造成160%武力伤害"),
        Map.of("id", 6, "name", "魅惑", "icon", "🦊", "type", "主动", "effectType", "控制", "effectValue", 50, "cooldown", 3, "cost", 25, "targetType", "单体", "description", "妲己魅惑，有50%概率使目标混乱"),
        
        // 被动战法
        Map.of("id", 101, "name", "莲花护体", "icon", "🪷", "type", "被动", "effectType", "增益", "effectValue", 10, "cooldown", 0, "cost", 0, "targetType", "自身", "description", "永久提升10%防御"),
        Map.of("id", 102, "name", "天眼通", "icon", "👁️", "type", "被动", "effectType", "增益", "effectValue", 15, "cooldown", 0, "cost", 0, "targetType", "自身", "description", "永久提升15%暴击率"),
        Map.of("id", 103, "name", "天命所归", "icon", "⭐", "type", "被动", "effectType", "增益", "effectValue", 20, "cooldown", 0, "cost", 0, "targetType", "自身", "description", "永久提升20%经验获取"),
        
        // 指挥战法
        Map.of("id", 201, "name", "鼓舞全军", "icon", "🎺", "type", "指挥", "effectType", "增益", "effectValue", 15, "cooldown", 0, "cost", 0, "targetType", "全体友军", "description", "战斗开始时，全队提升15%攻击力"),
        Map.of("id", 202, "name", "冲锋号角", "icon", "📯", "type", "指挥", "effectType", "增益", "effectValue", 10, "cooldown", 0, "cost", 0, "targetType", "全体友军", "description", "战斗开始时，全队提升10%速度"),
        
        // 联动战法
        Map.of("id", 301, "name", "父子同心", "icon", "👨‍👦", "type", "联动", "effectType", "增益", "effectValue", 25, "cooldown", 0, "cost", 0, "targetType", "特定武将", "description", "哪吒+李靖同时上阵，攻击力提升25%")
    );
}
