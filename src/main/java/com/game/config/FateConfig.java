package com.game.config;

import java.util.*;

/**
 * 缘分系统配置
 */
public class FateConfig {
    
    public enum FateType {
        ATK, HP, DEF, CRIT, DAMAGE
    }
    
    public static final List<Map<String, Object>> FATES = Arrays.asList(
        // 阐教缘分
        Map.of("id", 1, "name", "父子同心", "desc", "哪吒+李靖父子联手",
            "heroes", Arrays.asList("哪吒", "李靖"), "type", FateType.ATK, "value", 15, "icon", "👨👦"),
        Map.of("id", 2, "name", "阐教三兄弟", "desc", "金吒+木吒+哪吒三兄弟",
            "heroes", Arrays.asList("哪吒", "金吒", "木吒"), "type", FateType.ATK, "value", 20, "icon", "👬"),
        Map.of("id", 3, "name", "师徒情深", "desc", "杨戬+玉鼎真人师徒",
            "heroes", Arrays.asList("杨戬", "玉鼎真人"), "type", FateType.CRIT, "value", 10, "icon", "👨‍🎓"),
        
        // 截教缘分
        Map.of("id", 5, "name", "截教双圣", "desc", "通天教主+多宝道人",
            "heroes", Arrays.asList("通天教主", "多宝道人"), "type", FateType.ATK, "value", 20, "icon", "🗡️"),
        Map.of("id", 6, "name", "三霄合体", "desc", "云霄+琼霄+碧霄三姐妹",
            "heroes", Arrays.asList("云霄", "琼霄", "碧霄"), "type", FateType.HP, "value", 25, "icon", "🌸"),
        Map.of("id", 7, "name", "魔家四将", "desc", "魔礼青+魔礼红+魔礼海+魔礼寿",
            "heroes", Arrays.asList("魔礼青", "魔礼红", "魔礼海", "魔礼寿"), "type", FateType.DEF, "value", 20, "icon", "🌿"),
        Map.of("id", 8, "name", "哼哈二将", "desc", "郑伦+陈奇哼哈二将",
            "heroes", Arrays.asList("郑伦", "陈奇"), "type", FateType.CRIT, "value", 15, "icon", "🤨"),
        
        // 商朝缘分
        Map.of("id", 9, "name", "昏君佞臣", "desc", "纣王+妲己",
            "heroes", Arrays.asList("纣王", "妲己"), "type", FateType.ATK, "value", 15, "icon", "👑"),
        Map.of("id", 10, "name", "商朝忠臣", "desc", "比干+闻太师+黄飞虎",
            "heroes", Arrays.asList("比干", "闻太师", "黄飞虎"), "type", FateType.DEF, "value", 20, "icon", "🛡️"),
        
        // 周朝缘分
        Map.of("id", 11, "name", "文王武王", "desc", "姬昌+姬发父子",
            "heroes", Arrays.asList("姬昌", "姬发"), "type", FateType.ATK, "value", 20, "icon", "👑"),
        Map.of("id", 12, "name", "周公辅政", "desc", "姬发+周公旦",
            "heroes", Arrays.asList("姬发", "周公旦"), "type", FateType.HP, "value", 15, "icon", "📜"),
        Map.of("id", 13, "name", "姜尚辅周", "desc", "姜子牙+周武王",
            "heroes", Arrays.asList("姜子牙", "周武王"), "type", FateType.DAMAGE, "value", 20, "icon", "📿"),
        
        // 跨阵营
        Map.of("id", 14, "name", "封神大战", "desc", "姜子牙+申公豹对立",
            "heroes", Arrays.asList("姜子牙", "申公豹"), "type", FateType.CRIT, "value", 15, "icon", "⚔️"),
        Map.of("id", 15, "name", "殷商余孽", "desc", "殷郊+殷洪纣王之子",
            "heroes", Arrays.asList("殷郊", "殷洪"), "type", FateType.ATK, "value", 15, "icon", "🔥")
    );
}
