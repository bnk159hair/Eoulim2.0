import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';

export const Mobile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // @ts-ignore
    window.changePage = message => {
      const data = JSON.parse(message);
      console.log(data);
      if (data.invitation === false) {
        console.log('모바일에서 새 친구 세션으로 이동합니다.');
        navigate('/session', { state: { childId: data.childId, invitation: false } });
      } else if (data.invitation === true) {
        console.log('모바일에서 내 친구 세션으로 이동합니다.');
        navigate('/session', { state: { childId: data.childId, invitation: true } });
      } else {
        console.log('모바일에서 웹 세션으로 접속에 실패하였습니다.');
        console.log(message);
      }
    };
  }, []);

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
