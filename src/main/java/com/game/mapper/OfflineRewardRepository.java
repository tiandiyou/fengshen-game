package com.game.mapper;

import com.game.entity.OfflineReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OfflineRewardRepository extends JpaRepository<OfflineReward, Long> {
    
    Optional<OfflineReward> findByPlayerId(Long playerId);
}