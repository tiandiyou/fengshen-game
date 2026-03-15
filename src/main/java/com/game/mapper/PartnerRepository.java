package com.game.mapper;

import com.game.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    List<Partner> findByPlayerId(Long playerId);
}
