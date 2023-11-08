package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.FcmTokenEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

  Optional<FcmTokenEntity> findByChildIdAndToken(@NonNull Long childId, @NonNull String token);

  Optional<FcmTokenEntity> findByUserIdAndToken(@NonNull Long userId, @NonNull String token);

  List<FcmTokenEntity> findAllByChildId(@NonNull Long childId);
}
