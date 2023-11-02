package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {
}
