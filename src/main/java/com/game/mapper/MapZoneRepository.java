package com.game.mapper;

import com.game.entity.MapZone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MapZoneRepository extends JpaRepository<MapZone, Long> {
    Optional<MapZone> findByZoneId(Integer zoneId);
}
