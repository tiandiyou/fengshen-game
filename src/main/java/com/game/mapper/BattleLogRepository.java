package com.game.mapper;

import com.game.entity.BattleLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BattleLogRepository extends JpaRepository<BattleLog, Long> {
    List<BattleLog> findByPlayerIdOrderByBattleTimeDesc(Long playerId);
    Page<BattleLog> findByPlayerId(Long playerId, Pageable pageable);
}
