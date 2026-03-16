package com.game.mapper;

import com.game.entity.AllianceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AllianceMemberRepository extends JpaRepository<AllianceMember, Long> {
    List<AllianceMember> findByAllianceId(Long allianceId);
    Optional<AllianceMember> findByAllianceIdAndPlayerId(Long allianceId, Long playerId);
    Optional<AllianceMember> findByPlayerId(Long playerId);
}
