package com.game.config;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class HeroIconConfig {
    
    private static final Map<String, String> ICONS = new HashMap<>();
    
    static {
        // 阐教
        ICONS.put("燃灯道人", "🏆");
        ICONS.put("南极仙翁", "🦌");
        ICONS.put("广成子", "⚔️");
        ICONS.put("赤精子", "⚔️");
        ICONS.put("玉鼎真人", "📚");
        ICONS.put("太乙真人", "🔥");
        ICONS.put("清虚道德真君", "☯️");
        ICONS.put("普贤真人", "⚔️");
        ICONS.put("慈航道", "👸");
        ICONS.put("文殊广法天尊", "📖");
        ICONS.put("惧留孙", "⛓️");
        ICONS.put("道行天尊", "🌟");
        ICONS.put("姜子牙", "📜");
        ICONS.put("申公豹", "🐍");
        ICONS.put("雷震子", "⚡");
        ICONS.put("杨戬", "👁️");
        ICONS.put("哪吒", "🔥");
        ICONS.put("金吒", "⚔️");
        ICONS.put("木吒", "⚔️");
        ICONS.put("韦护", "⚖️");
        ICONS.put("李靖", "🏰");
        ICONS.put("金角大王", "👼");
        ICONS.put("银角大王", "👼");
        // 截教
        ICONS.put("通天教主", "🗡️");
        ICONS.put("多宝道人", "💰");
        ICONS.put("金灵圣母", "👸");
        ICONS.put("无当圣母", "🧘");
        ICONS.put("龟灵圣母", "🐢");
        ICONS.put("乌云仙", "🐍");
        ICONS.put("火灵圣母", "🔥");
        ICONS.put("罗宣", "🔥");
        ICONS.put("刘环", "🔥");
        ICONS.put("羽翼仙", "🦅");
        ICONS.put("闻仲", "👁️");
        ICONS.put("张桂芳", "⚔️");
        ICONS.put("陈奇", "⚔️");
        ICONS.put("郑伦", "⚔️");
        ICONS.put("余化", "⚔️");
        // 商朝
        ICONS.put("纣王", "👑");
        ICONS.put("妲己", "🦊");
        ICONS.put("比干", "💔");
        ICONS.put("黄飞虎", "⚔️");
        ICONS.put("黄飞豹", "⚔️");
        ICONS.put("崇侯虎", "⚔️");
        ICONS.put("崇黑虎", "⚔️");
        // 周朝
        ICONS.put("姬发", "👑");
        ICONS.put("姬昌", "📜");
        ICONS.put("周公旦", "📜");
        ICONS.put("姜尚", "📜");
        ICONS.put("散宜生", "📜");
        ICONS.put("太颠", "⚔️");
        ICONS.put("南官适", "⚔️");
        // 通用
        ICONS.put("士兵", "💂");
        ICONS.put("将领", "⚔️");
        ICONS.put("弓手", "🏹");
        ICONS.put("骑兵", "🐎");
        ICONS.put("法师", "🔮");
    }
    
    public String getIcon(String name) {
        return ICONS.getOrDefault(name, "⚔️");
    }
    
    public static String getQualityColor(String quality) {
        return switch (quality) {
            case "red" -> "#e74c3c";
            case "orange" -> "#e67e22";
            case "purple" -> "#9b59b6";
            case "blue" -> "#3498db";
            default -> "#888";
        };
    }
}
