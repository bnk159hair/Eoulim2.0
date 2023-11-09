import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';
import { changeAnimon, getAnimons } from 'apis/animonApis';
import { ModalOverlay, ModalContent, Animon } from './AnimonModalStyles';
import { Profilekey } from '../../atoms/Profile';

interface AnimonModalProps {
  onClose: () => void;
  profile: () => void;
}

interface AnimonInfo {
  id: number;
  bodyImagePath: string;
  maskImagePath: string;
  name: string;
}

const AnimonModal = ({ onClose, profile }: AnimonModalProps) => {
  const childId = useRecoilValue(Profilekey);
  const [animons, setAnimons] = useState<AnimonInfo[]>([]);

  getAnimons({
    childId,
    onSuccess: data => {
      setAnimons(data);
    },
    onError: () => {
      console.log('애니몬 리스트 조회에 실패했습니다.');
    },
  });

  const onClick = (animonId: number) => {
    changeAnimon({
      animonId,
      childId,
      onSuccess: () => {
        onClose();
        profile();
      },
      onError: () => {
        console.log('애니몬을 바꾸는데 실패했습니다.');
      },
    });
  };

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent>
        {animons.map(animon => (
          <Animon
            key={animon.id}
            onClick={() => onClick(animon.id)}
            animonurl={animon.bodyImagePath}
          />
        ))}
      </ModalContent>
    </ModalOverlay>
  );
};

export default AnimonModal;
