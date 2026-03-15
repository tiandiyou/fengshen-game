package com.game.mapper;

import com.game.entity.Cultivation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CultivationRepository extends JpaRepository<Cultivation, Long> {
    Optional<Cultivation> findByPlayerId(Long playerId);
}
