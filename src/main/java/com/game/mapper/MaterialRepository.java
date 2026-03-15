package com.game.mapper;

import com.game.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    Optional<Material> findByPlayerIdAndType(Long playerId, String type);
    List<Material> findByPlayerId(Long playerId);
}
