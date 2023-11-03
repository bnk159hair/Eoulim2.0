import React, { useEffect, useState } from 'react';
import { ModalOverlay, ModalContent, Cat, Dog, Panda, Tiger } from './AnimonModalStyles';
import { useRecoilValue } from 'recoil';
import { Profilekey } from '../../atoms/Profile';
import { tokenState } from '../../atoms/Auth';
import axios from 'axios';
import { API_BASE_URL } from '../../apis/urls';

interface AnimonModalProps {
  onClose: () => void;
  profile: () => void;
  animons: {
    id: number;
    headImagePath: string;
    bodyImagePath: string;
    name: string;
  };
}

const AnimonModal = ({ onClose, profile }: AnimonModalProps) => {
  const profileId = useRecoilValue(Profilekey);
  const token = useRecoilValue(tokenState);
  const [animons, setAnimons] = useState([]);

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

  const getAnimon = () => {
    axios
      .get(`${API_BASE_URL}/children/${profileId}/animons`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then(response => {
        setAnimons(response.data.data);
        console.log(response.data.data);
      })
      .catch(error => {
        console.log(error);
      });
  };

  useEffect(() => {
    getAnimon();
  }, [animons]);

  return (
    <>
      <ModalOverlay onClick={onClose}>
        <ModalContent>
          <Panda onClick={() => changeAnimon(1)}></Panda>
          <Dog onClick={() => changeAnimon(2)}></Dog>
          <Cat onClick={() => changeAnimon(3)}></Cat>
          <Tiger onClick={() => changeAnimon(4)}></Tiger>
        </ModalContent>
      </ModalOverlay>
    </>
  );
};

export default AnimonModal;
