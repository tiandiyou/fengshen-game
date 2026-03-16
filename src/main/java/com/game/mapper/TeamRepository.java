package com.game.mapper;

import com.game.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByPlayerId(Long playerId);
    List<Team> findByPlayerIdAndCityId(Long playerId, Long cityId);
    Optional<Team> findByPlayerIdAndTeamIndex(Long playerId, Integer teamIndex);
    Optional<Team> findByPlayerIdAndCityIdAndTeamIndex(Long playerId, Long cityId, Integer teamIndex);
    void deleteByPlayerIdAndCityId(Long playerId, Long cityId);
}
