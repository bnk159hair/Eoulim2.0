package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {}
