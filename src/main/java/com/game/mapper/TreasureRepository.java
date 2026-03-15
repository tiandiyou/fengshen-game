package com.game.mapper;

import com.game.entity.Treasure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreasureRepository extends JpaRepository<Treasure, Long> {
    List<Treasure> findByPlayerId(Long playerId);
}
