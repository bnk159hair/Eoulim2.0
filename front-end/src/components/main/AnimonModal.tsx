import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';
import { changeAnimon, getAnimons } from 'apis/animonApis';
import { ModalOverlay, ModalContent, Animon, BeforeButton, AfterButton } from './AnimonModalStyles';
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
  const [currentPage, setCurrentPage] = useState(1);
  const animonsPerPage = 3;

  getAnimons({
    childId,
    onSuccess: data => {
      setAnimons(data);
    },
    onError: () => {
      console.log('애니몬 리스트 조회에 실패했습니다.');
    },
  });

  const stopPropagation = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
  };

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

  const indexOfLastAnimon = currentPage * animonsPerPage;
  const indexOfFirstAnimon = indexOfLastAnimon - animonsPerPage;
  const currentAnimons = animons.slice(indexOfFirstAnimon, indexOfLastAnimon);

  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent onClick={stopPropagation}>
        {animons.length > animonsPerPage && (
          <div style={{ width: '50px' }}>
            {currentPage > 1 && <BeforeButton onClick={prevPage} />}
          </div>
        )}
        {currentAnimons.map(animon => (
          <Animon
            key={animon.id}
            onClick={() => onClick(animon.id)}
            animonurl={animon.maskImagePath}
          />
        ))}
        {animons.length > animonsPerPage && (
          <div style={{ width: '50px' }}>
            {currentPage < Math.ceil(animons.length / animonsPerPage) && (
              <AfterButton onClick={nextPage} />
            )}
          </div>
        )}
      </ModalContent>
    </ModalOverlay>
  );
};

export default AnimonModal;
