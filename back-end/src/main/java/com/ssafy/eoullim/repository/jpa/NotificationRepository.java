package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.NotificationEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findAllByUserId(@NonNull Long userId);

  @Modifying
  @Query("delete from NotificationEntity n where n.user.id = ?1")
  void deleteByUserId(@NonNull Long userId);
}
