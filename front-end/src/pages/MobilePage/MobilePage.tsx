import React from 'react';
import { useNavigate } from 'react-router';
import { Button } from '@mui/base';

export const Mobile = () => {
  const navigate = useNavigate();

  let childId = 0;
  let invitation = false;
  let token = '';

  const TOKEN = 'accessToken';
  const isAccessTokenEmpty = localStorage.getItem(TOKEN) === null;

  const saveToken = (token: string) => {
    if (isAccessTokenEmpty) {
      localStorage.setItem(TOKEN, token);
    } else {
      localStorage.removeItem(TOKEN);
      localStorage.setItem(TOKEN, token);
    }
  };

  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  window.changePage = message => {
    const data = JSON.parse(message);
    childId = data.childId;
    invitation = data.invitation;
    token = data.token;
    console.log(`invitation: ${invitation}, childId: ${childId}, token: ${token}`);
    if (token) {
      saveToken(token);
    }
  };

  const onClick = () => {
    if (childId && token && invitation === false) {
      console.log('모바일에서 새 친구 세션으로 이동합니다.');
      navigate('/session', { state: { childId, invitation } });
    } else if (childId && token && invitation === true) {
      console.log('모바일에서 내 친구 세션으로 이동합니다.');
      navigate('/session', { state: { childId, invitation } });
    } else {
      console.log('모바일에서 웹 세션으로 접속에 실패하였습니다.');
      console.log(invitation, childId, token);
    }
  };

  return (
    <Button color={'error'} onClick={onClick}>
      {'입장하기'}
    </Button>
  );
};

export default Mobile;
