package com.game.mapper;

import com.game.entity.GachaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GachaRecordRepository extends JpaRepository<GachaRecord, Long> {
    List<GachaRecord> findByPlayerIdOrderByGachaTimeDesc(Long playerId);
    List<GachaRecord> findByPlayerIdAndQuality(Long playerId, String quality);
}
