import React, { useState } from 'react';
import { TextField, IconButton, Button } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { getCheckUsername, postSignup } from 'apis/auth';
import inputAlert from 'utils/inputAlert';
import {
  ModalOverlay,
  ModalContent,
  ModalHeaderContainer,
  ModalFormContainer,
  IdContainer,
} from './SignupModalStyles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#b6d36f',
    },
  },
});

const SignupModal = ({ onClose }: { onClose: () => void }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirmation, setPasswordConfirmation] = useState('');
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [isUsernameUnique, setIsUsernameUnique] = useState(false);
  const [isPasswordMatch, setIsPasswordMatch] = useState(true);
  const phoneNumberPattern = /^010\d{8}$/;
  const namePattern = /^[가-힣]{2,17}$/;

  const handleSignup = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    if (
      !username.trim() ||
      !password.trim() ||
      !passwordConfirmation ||
      !name.trim() ||
      !phoneNumber
    ) {
      inputAlert('모든 정보를 입력해주세요!');
      return;
    }

    if (isUsernameUnique === false) {
      inputAlert('아이디 중복 확인을 해주세요!');
      return;
    }

    if (!isPasswordMatch) {
      inputAlert('비밀번호가 일치하지 않습니다!');
      return;
    }

    const isValidName = namePattern.test(name);
    if (!isValidName) {
      inputAlert('이름을 다시 입력해주세요!');
      return;
    }

    const isValidPhoneNumber = phoneNumberPattern.test(phoneNumber);
    if (!isValidPhoneNumber) {
      inputAlert('올바른 전화번호 형식이 아닙니다!');
      return;
    }

    const signupData = { username, password, name, phoneNumber };
    postSignup({
      signupData,
      onSuccess: () => {
        inputAlert('회원 가입에 성공했습니다!', false);
        onClose();
      },
      onError: () => {
        inputAlert('잠시 후 다시 시도해주세요!');
      },
    });
  };

  const handleCheckUsername = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    if (!username.trim()) {
      inputAlert('아이디를 입력해주세요!');
      return;
    }

    getCheckUsername({
      username,
      onSuccess: data => {
        setIsUsernameUnique(data);
        inputAlert('사용 가능한 아이디입니다!', false);
      },
      onError: () => {
        inputAlert('이미 사용 중인 아이디입니다!');
      },
    });
  };

  const handlePasswordConfirmation = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    const inputValue = event.target.value;
    setPasswordConfirmation(inputValue);
    setIsPasswordMatch(password === inputValue);
  };

  return (
    <ModalOverlay>
      <ModalContent>
        <ThemeProvider theme={theme}>
          <ModalHeaderContainer>
            <h2>{'회원가입'}</h2>
            <IconButton onClick={onClose}>
              <CloseIcon fontSize={'large'} />
            </IconButton>
          </ModalHeaderContainer>
          <ModalFormContainer>
            <IdContainer>
              <TextField
                label={'아이디'}
                variant={'outlined'}
                margin={'dense'}
                value={username}
                onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                  setUsername(event.target.value)
                }
                sx={{ width: '75%', marginTop: '0' }}
                helperText={isUsernameUnique && '중복 확인이 완료되었습니다.'}
                disabled={isUsernameUnique && true}
              />
              <Button
                variant={'contained'}
                size={'large'}
                sx={{ padding: '0.8rem', marginLeft: 'auto', fontSize: '16px' }}
                onClick={handleCheckUsername}
              >
                {'중복확인'}
              </Button>
            </IdContainer>
            <TextField
              label={'비밀번호'}
              variant={'outlined'}
              margin={'dense'}
              type={'password'}
              value={password}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setPassword(event.target.value)
              }
            />
            <TextField
              label={'비밀번호 확인'}
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
            <TextField
              label={'이름'}
              variant={'outlined'}
              margin={'dense'}
              value={name}
              placeholder={'홍길동'}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) => setName(event.target.value)}
            />
            <TextField
              label={'전화번호'}
              variant={'outlined'}
              margin={'dense'}
              value={phoneNumber}
              placeholder={'01012345678'}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setPhoneNumber(event.target.value)
              }
              helperText={"'-'없이 입력해주세요."}
            />
            <Button
              variant={'contained'}
              size={'large'}
              sx={{ padding: '0.6rem', marginTop: '1rem', fontSize: '20px' }}
              onClick={handleSignup}
            >
              {'가입완료'}
            </Button>
          </ModalFormContainer>
        </ThemeProvider>
      </ModalContent>
    </ModalOverlay>
  );
};

export default SignupModal;
