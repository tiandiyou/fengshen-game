package com.game.mapper;

import com.game.entity.PlayerMap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerMapRepository extends JpaRepository<PlayerMap, Long> {
    Optional<PlayerMap> findByPlayerId(Long playerId);
}
