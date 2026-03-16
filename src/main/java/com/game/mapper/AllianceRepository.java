package com.game.mapper;

import com.game.entity.Alliance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AllianceRepository extends JpaRepository<Alliance, Long> {
    Optional<Alliance> findByName(String name);
}
