import React from 'react';
import { useRecoilValue } from 'recoil';
import axios from 'axios';
import { ModalOverlay, ModalContent, Cat, Dog, Panda, Tiger } from './AnimonModalStyles';
import { Profilekey } from '../../atoms/Profile';
import { tokenState } from '../../atoms/Auth';
import { API_BASE_URL } from '../../apis/urls';

interface AnimonModalProps {
  onClose: () => void;
  profile: () => void;
}

const AnimonModal = ({ onClose, profile }: AnimonModalProps) => {
  const profileId = useRecoilValue(Profilekey);
  const token = useRecoilValue(tokenState);

  const changeAnimon = (id: number) => {
    axios
      .get(`${API_BASE_URL}/children/${profileId}/animons/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(() => {
        onClose();
        profile();
        console.log('바꾸기 완료');
      })
      .catch(error => {
        console.log('바꾸기 요청 오류', error);
      });
  };

  axios
    .get(`${API_BASE_URL}/children/${profileId}/animons`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then(response => {
      console.log(response.data.data);
    })
    .catch(error => {
      console.log(error);
    });

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent>
        <Panda onClick={() => changeAnimon(1)} />
        <Dog onClick={() => changeAnimon(2)} />
        <Cat onClick={() => changeAnimon(3)} />
        <Tiger onClick={() => changeAnimon(4)} />
      </ModalContent>
    </ModalOverlay>
  );
};

export default AnimonModal;
