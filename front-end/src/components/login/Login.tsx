import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TextField, InputAdornment } from '@mui/material';
import { AccountCircle } from '@mui/icons-material';
import KeyIcon from '@mui/icons-material/Key';
import { postLogin } from 'apis/authApis';
import inputAlert from 'utils/inputAlert';
import { useRecoilValue } from 'recoil';
import { fcmTokenState } from 'atoms/Firebase';
import { FormContainer, LoginButton, SignupContainer, SignupAnchor } from './LoginStyles';
import SignupModal from './SignupModal';

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const fcmToken = useRecoilValue(fcmTokenState);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleLogin = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!username.trim() || !password.trim()) {
      inputAlert('아이디와 비밀번호를 확인해주세요!');
      return;
    }
    let loginData;

    if (fcmToken) {
      loginData = { username, password, fcmToken };
    } else {
      loginData = { username, password, fcmToken: 'null' };
    }
    postLogin({
      loginData,
      onSuccess: () => {
        navigate('/profile');
      },
      onError: () => {
        inputAlert('아이디와 비밀번호를 확인해주세요!');
      },
    });
  };

  return (
    <>
      <FormContainer onSubmit={handleLogin}>
        <TextField
          label={'아이디'}
          variant={'filled'}
          color={'success'}
          margin={'dense'}
          value={username}
          sx={{
            bgcolor: '#F5EBC9',
            borderRadius: '5px',
            width: '80%',
          }}
          onChange={(event: React.ChangeEvent<HTMLInputElement>) => setUsername(event.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position={'start'}>
                <AccountCircle />
              </InputAdornment>
            ),
          }}
        />
        <TextField
          label={'비밀번호'}
          variant={'filled'}
          color={'success'}
          margin={'dense'}
          type={'password'}
          value={password}
          sx={{
            bgcolor: '#F5EBC9',
            borderRadius: '5px',
            width: '80%',
          }}
          onChange={(event: React.ChangeEvent<HTMLInputElement>) => setPassword(event.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position={'start'}>
                <KeyIcon />
              </InputAdornment>
            ),
          }}
        />
        <LoginButton type={'submit'}>{'로그인'}</LoginButton>
      </FormContainer>
      <SignupContainer>
        <span>{'아직 회원이 아니신가요?'}</span>
        <SignupAnchor onClick={() => setIsModalOpen(true)}>{'회원가입'}</SignupAnchor>
        {isModalOpen && <SignupModal onClose={() => setIsModalOpen(false)} />}
      </SignupContainer>
    </>
  );
};

export default Login;
