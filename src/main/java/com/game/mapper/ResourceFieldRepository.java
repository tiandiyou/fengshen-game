package com.game.mapper;

import com.game.entity.ResourceField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceFieldRepository extends JpaRepository<ResourceField, Long> {
    List<ResourceField> findByPlayerId(Long playerId);
    Optional<ResourceField> findByPlayerIdAndMapZoneId(Long playerId, Long mapZoneId);
}