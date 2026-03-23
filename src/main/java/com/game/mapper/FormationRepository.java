package com.game.mapper;

import com.game.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    
    List<Formation> findByPlayerId(Long playerId);
    
    Optional<Formation> findByPlayerIdAndIsDefault(Long playerId, Boolean isDefault);
    
    Optional<Formation> findByPlayerIdAndId(Long playerId, Long id);
}
