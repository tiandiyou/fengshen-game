package com.game.mapper;
import com.game.entity.WarCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface WarCreditRepository extends JpaRepository<WarCredit, Long> {
    Optional<WarCredit> findByPlayerIdAndSeasonId(Long playerId, Long seasonId);
}