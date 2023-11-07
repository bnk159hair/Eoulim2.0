package com.ssafy.eoullim.repository.jpa;

import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FollowEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByChildAndFollowingChild(ChildEntity child, ChildEntity followingChild);

    List<FollowEntity> findAllByChild(ChildEntity child);
}
