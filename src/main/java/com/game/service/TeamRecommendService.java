package com.game.service;

import com.game.entity.Partner;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TeamRecommendService {
    
    private static final Map<String, String> COUNTER = new HashMap<>();
    static {
        COUNTER.put("骑", "步");
        COUNTER.put("步", "弓");
        COUNTER.put("弓", "骑");
    }
    
    public Map<String, Object> recommend(List<Partner> enemyTeam, List<Partner> myPartners) {
        Map<String, Object> result = new HashMap<>();
        
        if (myPartners.size() < 3) {
            result.put("success", false);
            result.put("message", "伙伴不足3人");
            return result;
        }
        
        // 简化处理
        String enemyUnit = enemyTeam.isEmpty() ? "步" : enemyTeam.get(0).getType();
        if (enemyUnit == null) enemyUnit = "步";
        
        String counterUnit = COUNTER.getOrDefault(enemyUnit, "步");
        
        List<Partner> counterList = new ArrayList<>();
        List<Partner> otherList = new ArrayList<>();
        
        for (Partner p : myPartners) {
            String u = p.getType();
            if (u == null) u = "步";
            if (u.equals(counterUnit)) counterList.add(p);
            else otherList.add(p);
        }
        
        List<Map<String, Object>> recs = new ArrayList<>();
        
        if (counterList.size() >= 3) {
            Map<String, Object> r = new HashMap<>();
            r.put("type", "纯克制");
            r.put("reason", "针对" + enemyUnit + "用" + counterUnit);
            r.put("partners", counterList.subList(0, 3));
            recs.add(r);
        }
        
        if (recs.isEmpty() && myPartners.size() >= 3) {
            List<Partner> team = new ArrayList<>();
            if (!counterList.isEmpty()) team.add(counterList.get(0));
            Set<String> used = new HashSet<>();
            used.add(counterUnit);
            for (Partner p : myPartners) {
                String u = p.getType();
                if (u == null) u = "步";
                if (!used.contains(u) && team.size() < 3) {
                    team.add(p);
                    used.add(u);
                }
            }
            if (team.size() >= 3) {
                Map<String, Object> r = new HashMap<>();
                r.put("type", "混搭");
                r.put("reason", "多兵种组合");
                r.put("partners", team);
                recs.add(r);
            }
        }
        
        if (recs.isEmpty()) {
            myPartners.sort((a, b) -> power(b) - power(a));
            Map<String, Object> r = new HashMap<>();
            r.put("type", "高战力");
            r.put("reason", "最高战力组合");
            r.put("partners", myPartners.subList(0, 3));
            recs.add(r);
        }
        
        result.put("success", true);
        result.put("enemyUnit", enemyUnit);
        result.put("recommendations", recs);
        return result;
    }
    
    private int power(Partner p) {
        return p.getAtk() * 2 + p.getHp() / 10;
    }
}
