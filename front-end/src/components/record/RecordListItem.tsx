import React, { useState } from 'react';
import {
  RecordItemContainer,
  RecordProfileImg,
  OpponentImformation,
  RecordUrl,
} from './RecordListItemStyles';
import VideoModal from './RecordVideo';

interface RecordListItemProps {
  animonName: string;
  createTime: number[];
  school: string;
  videoPath: string;
  name: string;
  recordId: number;
}

const RecordListItem: React.FC<RecordListItemProps> = ({
  name,
  animonName,
  school,
  videoPath,
  createTime,
  recordId,
}) => {
  const IMGURL = `/${animonName}.png`;
  const [isModalOpen, setModalOpen] = useState(false);
  const openModal = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const formatTime = (timeline: number[]) => {
    const [y, mo, d, h, mi] = timeline;
    return `${y}. ${mo}. ${d}. ${h} : ${mi}`; // 원하는 형식으로 변환할 수 있습니다.
  };

  return (
    <RecordItemContainer>
      <RecordProfileImg style={{ backgroundImage: `url(${IMGURL})` }} />
      <OpponentImformation>
        <div>
          {'친구 이름 : '}
          {name}
        </div>
        <div>
          {'친구 학교 : '}
          {school}
          {'초등학교'}
        </div>
        <div>{`녹화 날짜 : ${formatTime(createTime)}`}</div>
      </OpponentImformation>
      {isModalOpen && <VideoModal onClose={closeModal} recordId={recordId} videoPath={videoPath} />}
      <RecordUrl onClick={openModal} />
    </RecordItemContainer>
  );
};

export default RecordListItem;
