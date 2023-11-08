import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { getRecord } from 'apis/recordApis';
import RecordListItem from '../../components/record/RecordListItem';
import { RecordPageContainer, EmptyRecord, Scroll, BackIcon } from './RecordPageStyles';
import { tokenState } from '../../atoms/Auth';
import { Profilekey } from '../../atoms/Profile';

interface Record {
  animonName: string;
  create_time: string;
  record_id: number;
  school: string;
  video_path: string;
  name: string;
  guide_seq: string;
  timeline: string;
}

const RecordPage = () => {
  const token = useRecoilValue(tokenState);
  const profileId = useRecoilValue(Profilekey);
  const [records, setRecords] = useState<Record[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    getRecord({
      profileId,
      onSuccess: data => {
        setRecords(data);
      },
      onError: () => {
        console.log('녹화 영상 불러오기 오류');
      },
    });
  }, [profileId, token]);

  const getBack = () => {
    navigate('/profile');
  };

  return (
    <RecordPageContainer>
      <BackIcon onClick={getBack} />
      {records.length > 0 ? (
        <Scroll>
          {records.map(record => (
            <RecordListItem
              key={record.record_id}
              name={record.name}
              animonName={record.animonName}
              school={record.school}
              video_path={record.video_path}
              create_time={record.create_time}
              guide_seq={record.guide_seq}
              timeline={record.timeline}
            />
          ))}
        </Scroll>
      ) : (
        <EmptyRecord />
      )}
    </RecordPageContainer>
  );
};

export default RecordPage;
