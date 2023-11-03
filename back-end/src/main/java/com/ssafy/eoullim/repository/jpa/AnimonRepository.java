package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.AnimonEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnimonRepository extends JpaRepository<AnimonEntity, Long> {
    @Query(value = "SELECT A FROM AnimonEntity A WHERE A.id BETWEEN 1 and 4")
    List<AnimonEntity> getDefaultAnimon();

    // 랜덤으로 limit 개수 만큼 animon 가져오기
    @Query(value = "SELECT * FROM animon ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    Optional<List<AnimonEntity>> findRandomAnimals(@Param("limit") int limit);
}
