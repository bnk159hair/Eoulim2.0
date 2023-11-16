package com.ssafy.eoullim.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.eoullim.model.Guide;
import com.ssafy.eoullim.model.RecordList;
import com.ssafy.eoullim.model.Record;
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

    public List<RecordList> findRecordsByChildId(Long id){
        return queryFactory.select(Projections.fields(RecordList.class,
                recordEntity.id, recordEntity.createdAt.as("createTime"), recordEntity.videoPath,
                        recordEntity.participant.name, recordEntity.participant.school, recordEntity.participant.profileAnimon.bodyImagePath.as("animonPath")))
                .from(recordEntity)
                .join(childEntity).on(childEntity.id.eq(recordEntity.participant.id))
                .where(recordEntity.master.id.eq(id))
                .orderBy(recordEntity.createdAt.desc())
                .fetch();
    }

    public Record findRecordById(Long id){
        return queryFactory.select(Projections.fields(Record.class,
                        recordEntity.id, recordEntity.createdAt.as("createTime"), recordEntity.videoPath,
                        recordEntity.participant.name, recordEntity.participant.school, recordEntity.participant.profileAnimon.bodyImagePath.as("animonPath")))
                .from(recordEntity)
                .join(childEntity).on(childEntity.id.eq(recordEntity.participant.id))
                .where(recordEntity.id.eq(id))
                .orderBy(recordEntity.createdAt.desc())
                .fetchOne();
    }

    public List<Guide> findGuideInfoById(Long id){
        return queryFactory.select(Projections.fields(Guide.class,
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
