package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository  extends JpaRepository<ChildEntity, Long> {
    Optional<List<ChildEntity>> findAllByUserId(Long userId);
}
