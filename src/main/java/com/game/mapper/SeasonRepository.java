package com.game.mapper;
import com.game.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Optional<Season> findByStatus(String status);
}