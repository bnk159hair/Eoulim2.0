import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';

export const Mobile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // @ts-ignore
    window.changePage = (message: Message) => {
      console.log(message);
      if (message.invitation === false) {
        console.log('모바일에서 새 친구 세션으로 이동합니다.');
        navigate('/session', { state: { childId: message.childId, invitation: false } });
      } else if (message.invitation === true) {
        console.log('모바일에서 내 친구 세션으로 이동합니다.');
        navigate('/session', { state: { childId: message.childId, invitation: true } });
      } else {
        console.log('모바일에서 웹 세션으로 접속에 실패하였습니다.');
        console.log(message);
      }
    };
  }, []);

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
