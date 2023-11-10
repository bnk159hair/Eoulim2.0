import React, { useEffect } from 'react';
import { useNavigate } from 'react-router';

export const Mobile = () => {
  const navigate = useNavigate();

  useEffect(() => {
    // @ts-ignore
    window.changePage = (message: Message) => {
      switch (message.invitation) {
        case false:
          navigate('/session', { state: { childId: message.childId, invitation: false } });
          break;
        case true:
          navigate('/session', { state: { childId: message.childId, invitation: true } });
          break;
        default:
          console.log('값이 존재하지 않습니다.');
          break;
      }
    };
  });

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
