package com.game.mapper;

import com.game.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByPlayerId(Long playerId);
    List<Equipment> findByPlayerIdAndEquipped(Long playerId, Boolean equipped);
    List<Equipment> findByPartnerId(Long partnerId);
    Optional<Equipment> findByIdAndPlayerId(Long id, Long playerId);
}
