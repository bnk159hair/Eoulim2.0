package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.entity.ChildAnimonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildAnimonRepository extends JpaRepository<ChildAnimonEntity, Long> {

    Optional<List<ChildAnimonEntity>> findAllByChildId(Long childId);

    Optional<ChildAnimonEntity> findByChildIdAndAnimonId(Long childId, Long animonId);
}
