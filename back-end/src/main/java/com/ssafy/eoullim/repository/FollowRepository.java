package com.ssafy.eoullim.repository;

import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {

    Optional<FollowEntity> findByChildAndFollowing(ChildEntity child, ChildEntity following);

    @Query(value = "select l.following from FollowEntity l where l.child = :child")
    List<ChildEntity> findFollowingsByChild(@Param("child") ChildEntity child);

}