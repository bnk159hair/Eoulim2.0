package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {
    Optional<FcmTokenEntity> findByChildId(@NonNull Long childId);

}
