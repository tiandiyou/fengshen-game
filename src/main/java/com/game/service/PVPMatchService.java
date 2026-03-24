package com.game.service;

import com.game.entity.Partner;
import com.game.entity.Player;
import com.game.entity.Team;
import com.game.mapper.PartnerRepository;
import com.game.mapper.PlayerRepository;
import com.game.mapper.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PVPMatchService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WarBattleService warBattleService;

    @Autowired
    private FormationService formationService;

    // 匹配缓存 (playerId -> 匹配信息)
    private final Map<Long, MatchInfo> matchQueue = new ConcurrentHashMap<>();

    // 战斗记录
    private final Map<Long, BattleRecord> battleRecords = new ConcurrentHashMap<>();

    // 排行榜缓存
    private List<Player> rankingCache = new ArrayList<>();
    private long rankingCacheTime = 0;
    private static final long RANKING_CACHE_TTL = 60000; // 1分钟

    /**
     * 匹配信息
     */
    public static class MatchInfo {
        public Long playerId;
        public Long cityId;
        public int power;
        public long matchTime;
        public String status; // matching, matched, completed

        public MatchInfo(Long playerId, Long cityId, int power) {
            this.playerId = playerId;
            this.cityId = cityId;
            this.power = power;
            this.matchTime = System.currentTimeMillis();
            this.status = "matching";
        }
    }

    /**
     * 战斗记录
     */
    public static class BattleRecord {
        public Long battleId;
        public Long playerId1;
        public Long playerId2;
        public int power1;
        public int power2;
        public String result; // player1_win, player2_win, draw
        public long battleTime;
        public Map<String, Object> detail;

        public BattleRecord(Long battleId, Long playerId1, Long playerId2, int power1, int power2, String result) {
            this.battleId = battleId;
            this.playerId1 = playerId1;
            this.playerId2 = playerId2;
            this.power1 = power1;
            this.power2 = power2;
            this.result = result;
            this.battleTime = System.currentTimeMillis();
        }
    }

    /**
     * 发起PVP匹配
     */
    public Map<String, Object> startMatch(Long playerId, Long cityId) {
        Map<String, Object> result = new HashMap<>();

        // 获取玩家战力
        int power = calcPlayerPower(playerId, cityId);
        if (power <= 0) {
            result.put("success", false);
            result.put("message", "请先设置队伍");
            return result;
        }

        // 检查是否已在匹配中
        MatchInfo existing = matchQueue.get(playerId);
        if (existing != null && "matching".equals(existing.status)) {
            // 返回当前匹配状态
            return findMatch(playerId);
        }

        // 创建匹配信息
        MatchInfo info = new MatchInfo(playerId, cityId, power);
        matchQueue.put(playerId, info);

        // 查找对手
        Long opponentId = findOpponent(playerId, power);
        if (opponentId != null) {
            // 找到对手，直接开始战斗
            MatchInfo opponentInfo = matchQueue.remove(opponentId);
            info.status = "matched";
            info.matchTime = System.currentTimeMillis();

            // 执行战斗
            return startBattle(playerId, opponentId, cityId);
        }

        // 返回匹配状态
        result.put("success", true);
        result.put("status", "matching");
        result.put("message", "正在匹配对手...");
        result.put("playerPower", power);
        result.put("queueSize", matchQueue.size());

        return result;
    }

    /**
     * 查找对手
     */
    private Long findOpponent(Long playerId, int power) {
        // 简单匹配：战力差距不超过30%
        int minPower = (int) (power * 0.7);
        int maxPower = (int) (power * 1.3);

        for (Map.Entry<Long, MatchInfo> entry : matchQueue.entrySet()) {
            if (entry.getKey().equals(playerId)) continue;

            MatchInfo info = entry.getValue();
            if (!"matching".equals(info.status)) continue;

            int oppPower = info.power;
            if (oppPower >= minPower && oppPower <= maxPower) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * 开始战斗
     */
    private Map<String, Object> startBattle(Long playerId1, Long playerId2, Long cityId) {
        Map<String, Object> result = new HashMap<>();

        // 获取双方队伍
        List<Partner> team1 = getTeamPartners(playerId1, cityId);
        List<Partner> team2 = getTeamPartners(playerId2, cityId);

        if (team1.isEmpty() || team2.isEmpty()) {
            result.put("success", false);
            result.put("message", "对手队伍未设置");
            return result;
        }

        // 计算战力
        int power1 = warBattleService.calcTeamPower(team1);
        int power2 = warBattleService.calcTeamPower(team2);

        // 执行战斗
        Map<String, Object> battle = warBattleService.battle(team1, team2);

        // 判断结果
        Integer damage1 = (Integer) battle.getOrDefault("totalDamage1", 0);
        Integer damage2 = (Integer) battle.getOrDefault("totalDamage2", 0);

        String battleResult;
        if (damage1 > damage2) {
            battleResult = "player1_win";
        } else if (damage2 > damage1) {
            battleResult = "player2_win";
        } else {
            battleResult = "draw";
        }

        // 记录战斗
        Long battleId = System.currentTimeMillis();
        BattleRecord record = new BattleRecord(battleId, playerId1, playerId2, power1, power2, battleResult);
        record.detail = battle;
        battleRecords.put(battleId, record);

        // 清理匹配队列
        matchQueue.remove(playerId1);
        matchQueue.remove(playerId2);

        // 返回结果
        result.put("success", true);
        result.put("battleId", battleId);
        result.put("result", battleResult);
        result.put("player1Power", power1);
        result.put("player2Power", power2);
        result.put("battle", battle);
        result.put("opponentId", playerId2);

        // 获取对手名称
        playerRepository.findById(playerId2).ifPresent(p ->
            result.put("opponentName", p.getName())
        );

        return result;
    }

    /**
     * 查询匹配状态
     */
    public Map<String, Object> findMatch(Long playerId) {
        Map<String, Object> result = new HashMap<>();

        MatchInfo info = matchQueue.get(playerId);
        if (info == null) {
            result.put("success", false);
            result.put("message", "没有进行中的匹配");
            return result;
        }

        if ("matched".equals(info.status)) {
            // 查找对手
            Long opponentId = findOpponent(playerId, info.power);
            if (opponentId != null) {
                return startBattle(playerId, opponentId, info.cityId);
            }
        }

        result.put("success", true);
        result.put("status", info.status);
        result.put("playerPower", info.power);
        result.put("queueSize", matchQueue.size());
        result.put("waitTime", System.currentTimeMillis() - info.matchTime);

        return result;
    }

    /**
     * 取消匹配
     */
    public Map<String, Object> cancelMatch(Long playerId) {
        Map<String, Object> result = new HashMap<>();

        MatchInfo info = matchQueue.remove(playerId);
        if (info == null) {
            result.put("success", false);
            result.put("message", "没有进行中的匹配");
        } else {
            result.put("success", true);
            result.put("message", "已取消匹配");
        }

        return result;
    }

    /**
     * 获取排行榜
     */
    public Map<String, Object> getRanking(int limit) {
        Map<String, Object> result = new HashMap<>();

        // 更新缓存
        long now = System.currentTimeMillis();
        if (rankingCache.isEmpty() || now - rankingCacheTime > RANKING_CACHE_TTL) {
            List<Player> players = playerRepository.findAll();
            rankingCache = players.stream()
                .sorted((p1, p2) -> Integer.compare(
                    p2.getZhanli() != null ? p2.getZhanli() : 0,
                    p1.getZhanli() != null ? p1.getZhanli() : 0
                ))
                .limit(limit)
                .collect(Collectors.toList());
            rankingCacheTime = now;
        }

        List<Map<String, Object>> ranking = new ArrayList<>();
        for (int i = 0; i < rankingCache.size(); i++) {
            Player p = rankingCache.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("rank", i + 1);
            item.put("playerId", p.getId());
            item.put("playerName", p.getName());
            item.put("power", p.getZhanli() != null ? p.getZhanli() : 0);
            item.put("level", p.getLevel());
            ranking.add(item);
        }

        result.put("success", true);
        result.put("ranking", ranking);

        return result;
    }

    /**
     * 获取战斗记录
     */
    public Map<String, Object> getBattleHistory(Long playerId, int limit) {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> battles = battleRecords.values().stream()
            .filter(r -> r.playerId1.equals(playerId) || r.playerId2.equals(playerId))
            .sorted((r1, r2) -> Long.compare(r2.battleTime, r1.battleTime))
            .limit(limit)
            .map(r -> {
                Map<String, Object> item = new HashMap<>();
                item.put("battleId", r.battleId);
                item.put("opponentId", r.playerId1.equals(playerId) ? r.playerId2 : r.playerId1);
                item.put("myPower", r.playerId1.equals(playerId) ? r.power1 : r.power2);
                item.put("opponentPower", r.playerId1.equals(playerId) ? r.power2 : r.power1);
                item.put("result", r.result);
                item.put("battleTime", r.battleTime);
                return item;
            })
            .collect(Collectors.toList());

        result.put("success", true);
        result.put("battles", battles);

        return result;
    }

    /**
     * 计算玩家战力
     */
    private int calcPlayerPower(Long playerId, Long cityId) {
        List<Partner> partners = getTeamPartners(playerId, cityId);
        if (partners.isEmpty()) return 0;
        return warBattleService.calcTeamPower(partners);
    }

    /**
     * 获取队伍伙伴
     */
    private List<Partner> getTeamPartners(Long playerId, Long cityId) {
        List<Partner> partners = new ArrayList<>();
        List<Team> teams = teamRepository.findByPlayerIdAndCityId(playerId, cityId);

        if (!teams.isEmpty()) {
            Team team = teams.get(0);
            if (team.getPartner1Id() != null) {
                partnerRepository.findById(team.getPartner1Id()).ifPresent(partners::add);
            }
            if (team.getPartner2Id() != null) {
                partnerRepository.findById(team.getPartner2Id()).ifPresent(partners::add);
            }
            if (team.getPartner3Id() != null) {
                partnerRepository.findById(team.getPartner3Id()).ifPresent(partners::add);
            }
        }

        return partners;
    }
}
