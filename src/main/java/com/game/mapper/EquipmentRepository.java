package com.game.mapper;
import com.game.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByPlayerId(Long playerId);
}