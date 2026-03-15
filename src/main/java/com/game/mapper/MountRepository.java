package com.game.mapper;

import com.game.entity.Mount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MountRepository extends JpaRepository<Mount, Long> {
    List<Mount> findByPlayerId(Long playerId);
    List<Mount> findByPlayerIdAndEquipped(Long playerId, Boolean equipped);
    Optional<Mount> findByIdAndPlayerId(Long id, Long playerId);
}
