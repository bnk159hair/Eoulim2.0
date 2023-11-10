import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';

export const Mobile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // @ts-ignore
    window.changePage = (message: Message) => {
      console.log(message);
      switch (message.invitation) {
        case false:
          console.log('모바일에서 새 친구 세션으로 이동합니다.');
          navigate('/session', { state: { childId: message.childId, invitation: false } });
          break;
        case true:
          console.log('모바일에서 내 친구 세션으로 이동합니다.');
          navigate('/session', { state: { childId: message.childId, invitation: true } });
          break;
        default:
          console.log('값이 존재하지 않습니다.');
          break;
      }
    };
  }, []);

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
