import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { Button, TextField } from '@mui/material';
import { postCheckPassword } from 'apis/authApis';
import inputAlert from 'utils/inputAlert';
import { Profilekey } from '../../atoms/Profile';
import { ModalOverlay, ModalContent, FormContainer, ButtonContainer } from './ToRecordModalStyles';

interface Props {
  onClose: () => void;
  childId: number;
}

const ToRecordModal = ({ onClose, childId }: Props) => {
  const navigate = useNavigate();
  const [password, setPassword] = useState('');
  const [, setProfileKey] = useRecoilState(Profilekey);

  const handlePasswordCheck = (
    event: React.MouseEvent<HTMLButtonElement> | React.FormEvent<HTMLFormElement>,
  ) => {
    event.preventDefault();
    postCheckPassword({
      password,
      onSuccess: isCorrect => {
        if (isCorrect) {
          setProfileKey(childId);
          inputAlert('비밀번호가 확인되었습니다!', false).then(() => navigate('/record'));
        } else {
          inputAlert('비밀번호를 확인해주세요!');
        }
      },
      onError: () => {
        inputAlert('잠시 후 다시 시도해주세요!');
      },
    });
  };

  return (
    <ModalOverlay>
      <ModalContent>
        <FormContainer onSubmit={handlePasswordCheck}>
          <h2>{'비밀번호 확인'}</h2>
          <TextField
            label={'비밀번호 확인'}
            variant={'outlined'}
            margin={'dense'}
            type={'password'}
            value={password}
            onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
              setPassword(event.target.value)
            }
          />
          <ButtonContainer>
            <Button
              variant={'contained'}
              size={'small'}
              sx={{ fontSize: '18px', margin: '0.5rem' }}
              onClick={handlePasswordCheck}
              fullWidth
            >
              {'확인'}
            </Button>
            <Button
              variant={'contained'}
              size={'small'}
              sx={{ fontSize: '18px', margin: '0.5rem' }}
              onClick={onClose}
              fullWidth
            >
              {'닫기'}
            </Button>
          </ButtonContainer>
        </FormContainer>
      </ModalContent>
    </ModalOverlay>
  );
};

export default ToRecordModal;
