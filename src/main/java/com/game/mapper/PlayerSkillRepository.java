package com.game.mapper;

import com.game.entity.PlayerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Long> {
    List<PlayerSkill> findByPlayerId(Long playerId);
    Optional<PlayerSkill> findByPlayerIdAndSkillId(Long playerId, Integer skillId);
}
