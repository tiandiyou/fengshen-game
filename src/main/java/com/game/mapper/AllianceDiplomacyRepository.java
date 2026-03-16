package com.game.mapper;
import com.game.entity.AllianceDiplomacy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AllianceDiplomacyRepository extends JpaRepository<AllianceDiplomacy, Long> {
    Optional<AllianceDiplomacy> findByAllianceId1AndAllianceId2(Long a1, Long a2);
}