package com.game.mapper;

import com.game.entity.Alliance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AllianceRepository extends JpaRepository<Alliance, Long> {
    Optional<Alliance> findByName(String name);
    List<Alliance> findByIdNotNullOrderByExpDesc();
}
