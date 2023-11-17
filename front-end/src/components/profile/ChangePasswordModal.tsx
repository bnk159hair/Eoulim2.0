import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, IconButton, TextField } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { postLogout, patchChangePassword } from 'apis/authApis';
import inputAlert from 'utils/inputAlert';
import { useRecoilValue } from 'recoil';
import { fcmTokenState } from 'atoms/Firebase';
import {
  ModalOverlay,
  ModalContent,
  FormContainer,
  HeaderContainer,
} from './ChangePasswordModalStyles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#b6d36f',
    },
  },
});

const ChangePasswordModal = ({ onClose }: { onClose: () => void }) => {
  const [curPassword, setCurPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [passwordConfirmation, setPasswordConfirmation] = useState('');
  const [isPasswordMatch, setIsPasswordMatch] = useState(true);
  const fcmToken = useRecoilValue(fcmTokenState);
  const navigate = useNavigate();

  const handlePasswordConfirmation = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const inputValue = event.target.value;
    setPasswordConfirmation(inputValue);
    setIsPasswordMatch(newPassword === inputValue);
  };

  const handleChangePassword = async (event: any) => {
    event?.preventDefault();
    if (!curPassword.trim() || !newPassword.trim()) {
      inputAlert('비밀번호를 입력해주세요!');
      return;
    }

    if (curPassword === newPassword) {
      inputAlert('새 비밀번호가 현재 비밀번호와 같습니다.');
      return;
    }

    if (!isPasswordMatch || passwordConfirmation !== newPassword) {
      setIsPasswordMatch(false);
      inputAlert('비밀번호 확인이 일치하지 않습니다!');
      return;
    }

    const passwordData = { curPassword, newPassword };
    patchChangePassword({
      passwordData,
      onSuccess: () => {
        inputAlert('비밀번호가 변경되었습니다!', false).then(() =>
          postLogout({
            fcmToken,
            onSuccess: () => {
              navigate('/login');
            },
            onError: () => {
              navigate('/login');
            },
          }),
        );
      },
      onError: () => {
        inputAlert('잠시 후 다시 시도해주세요!');
      },
    });
  };

  return (
    <ModalOverlay>
      <ModalContent>
        <ThemeProvider theme={theme}>
          <FormContainer onSubmit={handleChangePassword}>
            <HeaderContainer>
              <h2>{'비밀번호 변경'}</h2>
              <IconButton onClick={onClose}>
                <CloseIcon fontSize={'large'} />
              </IconButton>
            </HeaderContainer>

            <TextField
              label={'현재 비밀번호'}
              variant={'outlined'}
              margin={'dense'}
              type={'password'}
              value={curPassword}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setCurPassword(event.target.value)
              }
            />
            <TextField
              label={'새 비밀번호'}
              variant={'outlined'}
              margin={'dense'}
              type={'password'}
              value={newPassword}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setNewPassword(event.target.value)
              }
            />
            <TextField
              label={'새 비밀번호 확인'}
              variant={'outlined'}
              margin={'dense'}
              type={'password'}
              value={passwordConfirmation}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                handlePasswordConfirmation(event)
              }
              error={!isPasswordMatch}
              helperText={!isPasswordMatch && '비밀번호가 일치하지 않습니다.'}
            />
            <Button
              variant={'contained'}
              size={'large'}
              sx={{ padding: '0.6rem', marginTop: '1rem', fontSize: '18px' }}
              onClick={handleChangePassword}
              fullWidth
            >
              {'변경하기'}
            </Button>
          </FormContainer>
        </ThemeProvider>
      </ModalContent>
    </ModalOverlay>
  );
};

export default ChangePasswordModal;
