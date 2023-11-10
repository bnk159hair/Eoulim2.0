package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  List<NotificationEntity> findAllByUserId(@NonNull Long id);
}