package com.game.mapper;

import com.game.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByCityId(Long cityId);
    Optional<Building> findByCityIdAndType(Long cityId, String type);
}
