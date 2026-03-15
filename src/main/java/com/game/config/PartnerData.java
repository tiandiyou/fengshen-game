package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 武将数据配置
 * 包含100+武将，按颜色品质分类
 */
@Component
public class PartnerData {
    
    // ========== 红色武将 (SSR) - 10个 (隐藏于橙色中) ==========
    public static final List<Map<String, Object>> RED_PARTNERS = Arrays.asList(
        Map.of("id", 101, "name", "哪吒", "icon", "🔥", "quality", "red", "type", "骑",
            "atk", 180, "intelligence", 85, "lead", 90, "speed", 95, "politics", 30, "military", 95, "charm", 70,
            "growthAtk", 8, "growthInt", 4, "growthLead", 5,
            "skills", Arrays.asList("火尖枪", "莲花护体")),
        
        Map.of("id", 102, "name", "杨戬", "icon", "🐕", "quality", "red", "type", "步",
            "atk", 160, "intelligence", 80, "lead", 85, "speed", 90, "politics", 40, "military", 90, "charm", 60,
            "growthAtk", 7, "growthInt", 3, "growthLead", 4,
            "skills", Arrays.asList("八九玄功", "天眼通")),
        
        Map.of("id", 103, "name", "姜子牙", "icon", "📜", "quality", "red", "type", "策",
            "atk", 60, "intelligence", 140, "lead", 95, "speed", 70, "politics", 100, "military", 80, "charm", 90,
            "growthAtk", 2, "growthInt", 7, "growthLead", 5,
            "skills", Arrays.asList("封神榜", "天命所归")),
        
        Map.of("id", 104, "name", "纣王", "icon", "👑", "quality", "red", "type", "步",
            "atk", 170, "intelligence", 70, "lead", 95, "speed", 65, "politics", 80, "military", 90, "charm", 50,
            "growthAtk", 7, "growthInt", 3, "growthLead", 5,
            "skills", Arrays.asList("天子剑", "帝王之气")),
        
        Map.of("id", 105, "name", "妲己", "icon", "🦊", "quality", "red", "type", "策",
            "atk", 50, "intelligence", 200, "lead", 60, "speed", 110, "politics", 60, "military", 40, "charm", 100,
            "growthAtk", 2, "growthInt", 8, "growthLead", 3,
            "skills", Arrays.asList("魅惑", "倾城之貌")),
        
        Map.of("id", 106, "name", "雷震子", "icon", "⚡", "quality", "red", "type", "骑",
            "atk", 165, "intelligence", 70, "lead", 75, "speed", 105, "politics", 30, "military", 85, "charm", 55,
            "growthAtk", 7, "growthInt", 3, "growthLead", 3,
            "skills", Arrays.asList("风雷棍", "飞仙之翼")),
        
        Map.of("id", 107, "name", "申公豹", "icon", "🐆", "quality", "red", "type", "策",
            "atk", 55, "intelligence", 165, "lead", 75, "speed", 88, "politics", 70, "military", 75, "charm", 45,
            "growthAtk", 2, "growthInt", 7, "growthLead", 3,
            "skills", Arrays.asList("开天珠", "逆天改命")),
        
        Map.of("id", 108, "name", "赵公明", "icon", "💰", "quality", "red", "type", "步",
            "atk", 175, "intelligence", 60, "lead", 80, "speed", 75, "politics", 50, "military", 85, "charm", 40,
            "growthAtk", 7, "growthInt", 2, "growthLead", 4,
            "skills", Arrays.asList("定海珠", "财神附体")),
        
        Map.of("id", 109, "name", "闻仲", "icon", "👁️", "quality", "red", "type", "步",
            "atk", 150, "intelligence", 65, "lead", 90, "speed", 70, "politics", 60, "military", 95, "charm", 35,
            "growthAtk", 6, "growthInt", 3, "growthLead", 5,
            "skills", Arrays.asList("雌雄双鞭", "天眼通")),
        
        Map.of("id", 110, "name", "李靖", "icon", "🏰", "quality", "red", "type", "步",
            "atk", 120, "intelligence", 55, "lead", 85, "speed", 60, "politics", 90, "military", 80, "charm", 65,
            "growthAtk", 4, "growthInt", 2, "growthLead", 5,
            "skills", Arrays.asList("玲珑宝塔", "托塔天王"))
    );
    
    // ========== 橙色武将 (SR) - 30个 ==========
    public static final List<Map<String, Object>> ORANGE_PARTNERS = Arrays.asList(
        Map.of("id", 201, "name", "金吒", "icon", "🗡️", "quality", "orange", "type", "骑",
            "atk", 145, "intelligence", 50, "lead", 75, "speed", 85, "growthAtk", 5, "growthInt", 2, "growthLead", 3),
        
        Map.of("id", 202, "name", "木吒", "icon", "🎋", "quality", "orange", "type", "步",
            "atk", 140, "intelligence", 45, "lead", 70, "speed", 83, "growthAtk", 5, "growthInt", 2, "growthLead", 3),
        
        Map.of("id", 203, "name", "黄飞虎", "icon", "🐯", "quality", "orange", "type", "骑",
            "atk", 155, "intelligence", 40, "lead", 65, "speed", 80, "growthAtk", 6, "growthInt", 2, "growthLead", 3),
        
        Map.of("id", 204, "name", "杨任", "icon", "👓", "quality", "orange", "type", "策",
            "atk", 40, "intelligence", 120, "lead", 60, "speed", 75, "growthAtk", 2, "growthInt", 5, "growthLead", 2),
        
        Map.of("id", 205, "name", "李应", "icon", "💎", "quality", "orange", "type", "弓",
            "atk", 150, "intelligence", 35, "lead", 55, "speed", 78, "growthAtk", 5, "growthInt", 1, "growthLead", 2),
        
        Map.of("id", 206, "name", "崇黑虎", "icon", "🦅", "quality", "orange", "type", "骑",
            "atk", 145, "intelligence", 45, "lead", 60, "speed", 82, "growthAtk", 5, "growthInt", 2, "growthLead", 2),
        
        Map.of("id", 207, "name", "黄天化", "icon", "🔱", "quality", "orange", "type", "骑",
            "atk", 160, "intelligence", 50, "lead", 70, "speed", 88, "growthAtk", 6, "growthInt", 2, "growthLead", 3),
        
        Map.of("id", 208, "name", "土行孙", "icon", "⛏️", "quality", "orange", "type", "步",
            "atk", 130, "intelligence", 35, "lead", 80, "speed", 95, "growthAtk", 4, "growthInt", 1, "growthLead", 4),
        
        Map.of("id", 209, "name", "邓婵玉", "icon", "🏹", "quality", "orange", "type", "弓",
            "atk", 155, "intelligence", 55, "lead", 50, "speed", 92, "growthAtk", 5, "growthInt", 2, "growthLead", 2),
        
        Map.of("id", 210, "name", "郑伦", "icon", "🤨", "quality", "orange", "type", "步",
            "atk", 100, "intelligence", 30, "lead", 100, "speed", 65, "growthAtk", 3, "growthInt", 1, "growthLead", 5),
        
        Map.of("id", 211, "name", "陈奇", "icon", "😤", "quality", "orange", "type", "步",
            "atk", 95, "intelligence", 35, "lead", 95, "speed", 63, "growthAtk", 3, "growthInt", 1, "growthLead", 4),
        
        Map.of("id", 212, "name", "余化", "icon", "⚔️", "quality", "orange", "type", "骑",
            "atk", 120, "intelligence", 70, "lead", 80, "speed", 72, "growthAtk", 4, "growthInt", 3, "growthLead", 3),
        
        Map.of("id", 213, "name", "余元", "icon", "🧙", "quality", "orange", "type", "策",
            "atk", 30, "intelligence", 130, "lead", 90, "speed", 55, "growthAtk", 1, "growthInt", 6, "growthLead", 4),
        
        Map.of("id", 214, "name", "魔礼青", "icon", "🌿", "quality", "orange", "type", "步",
            "atk", 90, "intelligence", 25, "lead", 110, "speed", 50, "growthAtk", 3, "growthInt", 1, "growthLead", 5),
        
        Map.of("id", 215, "name", "魔礼红", "icon", "🔥", "quality", "orange", "type", "策",
            "atk", 35, "intelligence", 100, "lead", 85, "speed", 52, "growthAtk", 1, "growthInt", 4, "growthLead", 4),
        
        Map.of("id", 216, "name", "魔礼海", "icon", "🌊", "quality", "orange", "type", "弓",
            "atk", 135, "intelligence", 45, "lead", 55, "speed", 70, "growthAtk", 5, "growthInt", 2, "growthLead", 2),
        
        Map.of("id", 217, "name", "魔礼寿", "icon", "🐍", "quality", "orange", "type", "骑",
            "atk", 125, "intelligence", 40, "lead", 65, "speed", 78, "growthAtk", 4, "growthInt", 1, "growthLead", 3),
        
        Map.of("id", 218, "name", "洪锦", "icon", "🚩", "quality", "orange", "type", "骑",
            "atk", 110, "intelligence", 55, "lead", 60, "speed", 76, "growthAtk", 4, "growthInt", 2, "growthLead", 2),
        
        Map.of("id", 219, "name", "龙吉公主", "icon", "👸", "quality", "orange", "type", "策",
            "atk", 45, "intelligence", 115, "lead", 65, "speed", 85, "growthAtk", 2, "growthInt", 5, "growthLead", 2),
        
        Map.of("id", 220, "name", "殷郊", "icon", "👹", "quality", "orange", "type", "步",
            "atk", 165, "intelligence", 30, "lead", 75, "speed", 68, "growthAtk", 6, "growthInt", 1, "growthLead", 3),
        
        Map.of("id", 221, "name", "殷洪", "icon", "👺", "quality", "orange", "type", "策",
            "atk", 35, "intelligence", 110, "lead", 70, "speed", 72, "growthAtk", 1, "growthInt", 5, "growthLead", 3),
        
        Map.of("id", 222, "name", "姜文焕", "icon", "⚔️", "quality", "orange", "type", "骑",
            "atk", 140, "intelligence", 35, "lead", 65, "speed", 77, "growthAtk", 5, "growthInt", 1, "growthLead", 3),
        
        Map.of("id", 223, "name", "鄂顺", "icon", "🖌️", "quality", "orange", "type", "弓",
            "atk", 145, "intelligence", 40, "lead", 50, "speed", 80, "growthAtk", 5, "growthInt", 1, "growthLead", 2),
        
        Map.of("id", 224, "name", "伯邑考", "icon", "🎵", "quality", "orange", "type", "策",
            "atk", 30, "intelligence", 100, "lead", 55, "speed", 60, "growthAtk", 1, "growthInt", 4, "growthLead", 2),
        
        Map.of("id", 225, "name", "周文王", "icon", "📿", "quality", "orange", "type", "策",
            "atk", 25, "intelligence", 95, "lead", 60, "speed", 45, "growthAtk", 1, "growthInt", 4, "growthLead", 2),
        
        Map.of("id", 226, "name", "周武王", "icon", "⚖️", "quality", "orange", "type", "步",
            "atk", 80, "intelligence", 70, "lead", 85, "speed", 50, "growthAtk", 2, "growthInt", 3, "growthLead", 4),
        
        Map.of("id", 227, "name", "比干", "icon", "💔", "quality", "orange", "type", "策",
            "atk", 20, "intelligence", 105, "lead", 50, "speed", 55, "growthAtk", 1, "growthInt", 4, "growthLead", 2),
        
        Map.of("id", 228, "name", "箕子", "icon", "🎋", "quality", "orange", "type", "策",
            "atk", 25, "intelligence", 95, "lead", 55, "speed", 52, "growthAtk", 1, "growthInt", 4, "growthLead", 2),
        
        Map.of("id", 229, "name", "微子启", "icon", "📜", "quality", "orange", "type", "策",
            "atk", 30, "intelligence", 85, "lead", 50, "speed", 48, "growthAtk", 1, "growthInt", 3, "growthLead", 2),
        
        Map.of("id", 230, "name", "微子衍", "icon", "📕", "quality", "orange", "type", "策",
            "atk", 28, "intelligence", 80, "lead", 48, "speed", 46, "growthAtk", 1, "growthInt", 3, "growthLead", 2)
    );
    
    // ========== 紫色武将 (R) - 30个 ==========
    public static final List<Map<String, Object>> PURPLE_PARTNERS = Arrays.asList(
        Map.of("id", 301, "name", "太颠", "icon", "⚔️", "quality", "purple", "type", "步", "atk", 80, "intelligence", 20, "lead", 60, "speed", 40),
        Map.of("id", 302, "name", "闳夭", "icon", "📜", "quality", "purple", "type", "策", "atk", 20, "intelligence", 60, "lead", 40, "speed", 35),
        Map.of("id", 303, "name", "散宜生", "icon", "🎋", "quality", "purple", "type", "策", "atk", 18, "intelligence", 65, "lead", 35, "speed", 38),
        Map.of("id", 304, "name", "南宮适", "icon", "🏹", "quality", "purple", "type", "弓", "atk", 85, "intelligence", 15, "lead", 40, "speed", 45),
        Map.of("id", 305, "name", "毛公遂", "icon", "💼", "quality", "purple", "type", "步", "atk", 70, "intelligence", 25, "lead", 55, "speed", 42),
        Map.of("id", 306, "name", "周公旦", "icon", "📿", "quality", "purple", "type", "策", "atk", 15, "intelligence", 70, "lead", 45, "speed", 40),
        Map.of("id", 307, "name", "召公奭", "icon", "⚖️", "quality", "purple", "type", "策", "atk", 12, "intelligence", 65, "lead", 40, "speed", 35),
        Map.of("id", 308, "name", "毕公高", "icon", "👑", "quality", "purple", "type", "步", "atk", 65, "intelligence", 30, "lead", 50, "speed", 38),
        Map.of("id", 309, "name", "蔡叔度", "icon", "🚪", "quality", "purple", "type", "步", "atk", 60, "intelligence", 20, "lead", 55, "speed", 36),
        Map.of("id", 310, "name", "管叔鲜", "icon", "🏰", "quality", "purple", "type", "步", "atk", 55, "intelligence", 25, "lead", 50, "speed", 34),
        Map.of("id", 311, "name", "霍叔处", "icon", "🛡️", "quality", "purple", "type", "步", "atk", 50, "intelligence", 15, "lead", 60, "speed", 32),
        Map.of("id", 312, "name", "曹触伯", "icon", "⚔️", "quality", "purple", "type", "骑", "atk", 75, "intelligence", 20, "lead", 45, "speed", 48),
        Map.of("id", 313, "name", "罗宣", "icon", "🔥", "quality", "purple", "type", "策", "atk", 25, "intelligence", 80, "lead", 35, "speed", 42),
        Map.of("id", 314, "name", "刘环", "icon", "🌪️", "quality", "purple", "type", "策", "atk", 20, "intelligence", 75, "lead", 40, "speed", 40),
        Map.of("id", 315, "name", "火德星君", "icon", "☀️", "quality", "purple", "type", "策", "atk", 30, "intelligence", 85, "lead", 30, "speed", 45),
        Map.of("id", 316, "name", "水德星君", "icon", "🌙", "quality", "purple", "type", "策", "atk", 15, "intelligence", 90, "lead", 35, "speed", 38),
        Map.of("id", 317, "name", "千里眼", "icon", "👁️", "quality", "purple", "type", "策", "atk", 15, "intelligence", 60, "lead", 30, "speed", 40),
        Map.of("id", 318, "name", "顺风耳", "icon", "👂", "quality", "purple", "type", "策", "atk", 12, "intelligence", 55, "lead", 25, "speed", 45),
        Map.of("id", 319, "name", "高友乾", "icon", "⚔️", "quality", "purple", "type", "骑", "atk", 90, "intelligence", 25, "lead", 45, "speed", 50),
        Map.of("id", 320, "name", "李平", "icon", "📜", "quality", "purple", "type", "策", "atk", 22, "intelligence", 65, "lead", 40, "speed", 38),
        Map.of("id", 321, "name", "陈桐", "icon", "🖌️", "quality", "purple", "type", "弓", "atk", 95, "intelligence", 20, "lead", 35, "speed", 52),
        Map.of("id", 322, "name", "陈梧", "icon", "🏹", "quality", "purple", "type", "弓", "atk", 90, "intelligence", 18, "lead", 40, "speed", 48),
        Map.of("id", 323, "name", "张凤", "icon", "🦅", "quality", "purple", "type", "骑", "atk", 85, "intelligence", 22, "lead", 42, "speed", 50),
        Map.of("id", 324, "name", "赵升", "icon", "⚔️", "quality", "purple", "type", "步", "atk", 75, "intelligence", 18, "lead", 55, "speed", 40),
        Map.of("id", 325, "name", "孙焰红", "icon", "🔥", "quality", "purple", "type", "策", "atk", 28, "intelligence", 70, "lead", 32, "speed", 42),
        Map.of("id", 326, "name", "欧阳乙", "icon", "📜", "quality", "purple", "type", "策", "atk", 20, "intelligence", 60, "lead", 38, "speed", 36),
        Map.of("id", 327, "name", "王豹", "icon", "🐆", "quality", "purple", "type", "骑", "atk", 80, "intelligence", 20, "lead", 48, "speed", 46),
        Map.of("id", 328, "name", "韩升", "icon", "⚔️", "quality", "purple", "type", "步", "atk", 70, "intelligence", 15, "lead", 60, "speed", 38),
        Map.of("id", 329, "name", "韩变", "icon", "🔄", "quality", "purple", "type", "步", "atk", 68, "intelligence", 18, "lead", 55, "speed", 36),
        Map.of("id", 330, "name", "林善", "icon", "🌲", "quality", "purple", "type", "步", "atk", 65, "intelligence", 15, "lead", 50, "speed", 35)
    );
    
    // ========== 蓝色武将 (N) - 30个 ==========
    public static final List<Map<String, Object>> BLUE_PARTNERS = Arrays.asList(
        Map.of("id", 401, "name", "士兵甲", "icon", "💂", "quality", "blue", "type", "步", "atk", 30, "intelligence", 10, "lead", 30, "speed", 20),
        Map.of("id", 402, "name", "士兵乙", "icon", "💂", "quality", "blue", "type", "步", "atk", 28, "intelligence", 10, "lead", 28, "speed", 20),
        Map.of("id", 403, "name", "士兵丙", "icon", "💂", "quality", "blue", "type", "步", "atk", 25, "intelligence", 8, "lead", 25, "speed", 18),
        Map.of("id", 404, "name", "弓手", "icon", "🏹", "quality", "blue", "type", "弓", "atk", 35, "intelligence", 8, "lead", 15, "speed", 22),
        Map.of("id", 405, "name", "轻骑", "icon", "🐎", "quality", "blue", "type", "骑", "atk", 32, "intelligence", 5, "lead", 20, "speed", 30),
        Map.of("id", 406, "name", "盾牌兵", "icon", "🛡️", "quality", "blue", "type", "步", "atk", 20, "intelligence", 5, "lead", 40, "speed", 15),
        Map.of("id", 407, "name", "枪兵", "icon", "🔱", "quality", "blue", "type", "步", "atk", 28, "intelligence", 8, "lead", 25, "speed", 18),
        Map.of("id", 408, "name", "刀兵", "icon", "🔪", "quality", "blue", "type", "步", "atk", 30, "intelligence", 5, "lead", 22, "speed", 20),
        Map.of("id", 409, "name", "斥候", "icon", "👀", "quality", "blue", "type", "骑", "atk", 25, "intelligence", 12, "lead", 15, "speed", 35),
        Map.of("id", 410, "name", "文书", "icon", "📜", "quality", "blue", "type", "策", "atk", 8, "intelligence", 25, "lead", 15, "speed", 18),
        Map.of("id", 411, "name", "伙夫", "icon", "👨‍🍳", "quality", "blue", "type", "步", "atk", 15, "intelligence", 10, "lead", 20, "speed", 15),
        Map.of("id", 412, "name", "马夫", "icon", "🐴", "quality", "blue", "type", "步", "atk", 18, "intelligence", 8, "lead", 18, "speed", 20),
        Map.of("id", 413, "name", "民夫", "icon", "👷", "quality", "blue", "type", "步", "atk", 12, "intelligence", 5, "lead", 15, "speed", 12),
        Map.of("id", 414, "name", "商贩", "icon", "🏪", "quality", "blue", "type", "步", "atk", 10, "intelligence", 15, "lead", 12, "speed", 15),
        Map.of("id", 415, "name", "更夫", "icon", "🔔", "quality", "blue", "type", "步", "atk", 8, "intelligence", 10, "lead", 10, "speed", 18),
        Map.of("id", 416, "name", "轿夫", "icon", "🎎", "quality", "blue", "type", "步", "atk", 14, "intelligence", 5, "lead", 16, "speed", 16),
        Map.of("id", 417, "name", "纤夫", "icon", "🐂", "quality", "blue", "type", "步", "atk", 16, "intelligence", 5, "lead", 18, "speed", 14),
        Map.of("id", 418, "name", "税吏", "icon", "📊", "quality", "blue", "type", "策", "atk", 5, "intelligence", 20, "lead", 10, "speed", 12),
        Map.of("id", 419, "name", "狱卒", "icon", "🔒", "quality", "blue", "type", "步", "atk", 22, "intelligence", 8, "lead", 25, "speed", 18),
        Map.of("id", 420, "name", "城门卫", "icon", "🚪", "quality", "blue", "type", "步", "atk", 20, "intelligence", 10, "lead", 30, "speed", 15),
        Map.of("id", 421, "name", "巡捕", "icon", "🔍", "quality", "blue", "type", "步", "atk", 25, "intelligence", 12, "lead", 20, "speed", 22),
        Map.of("id", 422, "name", "挑夫", "icon", "📦", "quality", "blue", "type", "步", "atk", 12, "intelligence", 5, "lead", 14, "speed", 14),
        Map.of("id", 423, "name", "脚夫", "icon", "👣", "quality", "blue", "type", "步", "atk", 14, "intelligence", 5, "lead", 12, "speed", 16),
        Map.of("id", 424, "name", "向导", "icon", "🧭", "quality", "blue", "type", "步", "atk", 10, "intelligence", 18, "lead", 10, "speed", 25),
        Map.of("id", 425, "name", "小贩", "icon", "🎒", "quality", "blue", "type", "步", "atk", 8, "intelligence", 15, "lead", 8, "speed", 18),
        Map.of("id", 426, "name", "杂役", "icon", "🧹", "quality", "blue", "type", "步", "atk", 10, "intelligence", 8, "lead", 12, "speed", 12),
        Map.of("id", 427, "name", "门童", "icon", "🚪", "quality", "blue", "type", "步", "atk", 12, "intelligence", 10, "lead", 15, "speed", 14),
        Map.of("id", 428, "name", "丫鬟", "icon", "👩", "quality", "blue", "type", "步", "atk", 8, "intelligence", 12, "lead", 10, "speed", 16),
        Map.of("id", 429, "name", "家丁", "icon", "🏃", "quality", "blue", "type", "步", "atk", 18, "intelligence", 8, "lead", 16, "speed", 20),
        Map.of("id", 430, "name", "护卫", "icon", "💪", "quality", "blue", "type", "步", "atk", 25, "intelligence", 10, "lead", 22, "speed", 20)
    );
    
    // 获取所有武将
    public static List<Map<String, Object>> getAllPartners() {
        List<Map<String, Object>> all = new ArrayList<>();
        all.addAll(RED_PARTNERS);
        all.addAll(ORANGE_PARTNERS);
        all.addAll(PURPLE_PARTNERS);
        all.addAll(BLUE_PARTNERS);
        return all;
    }
    
    // 获取红将(隐藏)
    public static List<Map<String, Object>> getRedPartners() {
        return RED_PARTNERS;
    }
    
    // 获取橙将
    public static List<Map<String, Object>> getOrangePartners() {
        return ORANGE_PARTNERS;
    }
    
    // 获取紫将
    public static List<Map<String, Object>> getPurplePartners() {
        return PURPLE_PARTNERS;
    }
    
    // 获取蓝将
    public static List<Map<String, Object>> getBluePartners() {
        return BLUE_PARTNERS;
    }
}
