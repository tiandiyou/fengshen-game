package com.game.mapper;

import com.game.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByPlayerIdAndCityId(Long playerId, Long cityId);
    List<Resource> findByPlayerId(Long playerId);
}
