package com.ssafy.eoullim.repository.JPA;

import com.ssafy.eoullim.model.entity.RecordEntity;
import com.ssafy.eoullim.model.entity.RecordGuideEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordGuideRepository extends JpaRepository<RecordGuideEntity, Long> {
}
