import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { getRecords } from 'apis/recordApis';
import RecordListItem from '../../components/record/RecordListItem';
import { RecordPageContainer, EmptyRecord, Scroll, BackIcon } from './RecordPageStyles';
import { Profilekey } from '../../atoms/Profile';

interface Record {
  animonPath: string;
  createTime: number[];
  id: number;
  name: string;
  school: string;
  videoPath: string;
}

const RecordPage = () => {
  const childId = useRecoilValue(Profilekey);
  const [records, setRecords] = useState<Record[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    getRecords({
      childId,
      onSuccess: data => {
        console.log(data);
        setRecords(data);
      },
      onError: () => {
        console.log('녹화 영상 리스트 불러오기 오류');
      },
    });
  }, []);

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
              key={record.id}
              recordId={record.id}
              name={record.name}
              animonPath={record.animonPath}
              school={record.school}
              videoPath={record.videoPath}
              createTime={record.createTime}
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
