package com.ssafy.eoullim.repository.JPA;

import com.ssafy.eoullim.model.entity.GuideEntity;
import com.ssafy.eoullim.model.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<GuideEntity, Long> {
}
