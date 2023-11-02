package com.ssafy.eoullim.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.eoullim.dto.response.Record.GuideDTO;
import com.ssafy.eoullim.dto.response.Record.RecordListResponse;
import com.ssafy.eoullim.dto.response.Record.RecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ssafy.eoullim.model.entity.QRecordEntity.recordEntity;
import static com.ssafy.eoullim.model.entity.QGuideEntity.guideEntity;
import static com.ssafy.eoullim.model.entity.QRecordGuideEntity.recordGuideEntity;
import static com.ssafy.eoullim.model.entity.QChildEntity.childEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecordQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<RecordListResponse> findRecordsByChildId(Long id){
        return queryFactory.select(Projections.fields(RecordListResponse.class,
                recordEntity.id, recordEntity.createdAt.as("createTime"), recordEntity.videoPath,
                        recordEntity.participant.name, recordEntity.participant.school, recordEntity.participant.animon.name))
                .from(recordEntity)
                .join(childEntity).on(childEntity.id.eq(recordEntity.participant.id))
                .where(recordEntity.master.id.eq(id))
                .orderBy(recordEntity.createdAt.desc())
                .fetch();
    }

    public RecordResponse findRecordById(Long id){
        return queryFactory.select(Projections.fields(RecordResponse.class,
                        recordEntity.id, recordEntity.createdAt.as("createTime"), recordEntity.videoPath,
                        recordEntity.participant.name, recordEntity.participant.school, recordEntity.participant.animon.name))
                .from(recordEntity)
                .join(childEntity).on(childEntity.id.eq(recordEntity.participant.id))
                .where(recordEntity.id.eq(id))
                .orderBy(recordEntity.createdAt.desc())
                .fetchOne();
    }

    public List<GuideDTO> findGuideInfoById(Long id){
        return queryFactory.select(Projections.fields(GuideDTO.class,
                guideEntity.content, recordGuideEntity.timeline
                ))
                .from(guideEntity)
                .join(recordGuideEntity).on(recordGuideEntity.guide.id.eq(guideEntity.id))
                .join(recordEntity).on((recordEntity.id.eq(recordGuideEntity.record.id)))
                .where(recordEntity.id.eq(id))
                .orderBy(recordGuideEntity.sequence.asc())
                .fetch();
    }
}
