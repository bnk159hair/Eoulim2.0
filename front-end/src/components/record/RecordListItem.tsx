import React, { useState } from 'react';
import formatTime from 'utils/formatTime';
import {
  RecordItemContainer,
  RecordProfileImg,
  OpponentImformation,
  RecordUrl,
} from './RecordListItemStyles';
import VideoModal from './RecordVideo';

interface RecordListItemProps {
  animonPath: string;
  createTime: number[];
  school: string;
  videoPath: string;
  name: string;
  recordId: number;
}

const RecordListItem: React.FC<RecordListItemProps> = ({
  name,
  animonPath,
  school,
  videoPath,
  createTime,
  recordId,
}) => {
  const IMGURL = `${animonPath}`;
  console.log(IMGURL);
  const [isModalOpen, setModalOpen] = useState(false);
  const openModal = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
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
          {'친구 학교: '}
          {school}
        </div>
        <div>{`녹화 날짜 : ${formatTime(createTime)}`}</div>
      </OpponentImformation>
      {isModalOpen && <VideoModal onClose={closeModal} recordId={recordId} videoPath={videoPath} />}
      <RecordUrl onClick={openModal} />
    </RecordItemContainer>
  );
};

export default RecordListItem;
