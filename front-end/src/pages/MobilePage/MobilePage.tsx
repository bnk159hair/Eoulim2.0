import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';

export const Mobile = () => {
  const navigate = useNavigate();

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

  useEffect(() => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    window.changePage = message => {
      const data = JSON.parse(message);
      console.log(`invitation: ${data.invitation}, childId: ${data.childId}`);
      if (data.invitation === false) {
        const { invitation, childId, token } = JSON.parse(message);
        console.log(`invitation: ${invitation}, childId: ${childId}, token: ${token}`);
        if (token) {
          saveToken(token);
        }
        if (childId && token && invitation === false) {
          console.log('모바일에서 새 친구 세션으로 이동합니다.');
          navigate('/session', { state: { childId: data.childId, invitation: false } });
        } else if (data.invitation === true) {
          navigate('/session', { state: { childId, invitation } });
        } else if (childId && token && invitation === true) {
          console.log('모바일에서 내 친구 세션으로 이동합니다.');
          navigate('/session', { state: { childId: data.childId, invitation: true } });
          navigate('/session', { state: { childId, invitation } });
        } else {
          console.log('모바일에서 웹 세션으로 접속에 실패하였습니다.');
          console.log(data);
          console.log(typeof data);
          console.log(invitation, childId, token);
        }
      }
    };
  }, []);

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
