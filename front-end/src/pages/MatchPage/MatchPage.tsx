import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import { useWebSocket } from 'hooks/useWebSocket';
import Loading from '../../components/stream/Loading';
import { getUserInfo } from '../../apis/openViduApis';

interface UserData {
  childId: number;
  gender: string;
  grade: number;
  name: string;
  school: string;
}

const MatchPage = () => {
  const { state } = useLocation();
  const navigate = useNavigate();
  const { childId } = state;

  const [send, setSend] = useState(false);

  const [client, setClient] = useState<Client | null>(null);

  useWebSocket({
    onConnect(_, client) {
      setClient(client);
      client.subscribe(`/topic/random/start/${childId}`, response => {
        console.log('메시지 수신:', response.body);
        const { sessionId, token, guideSeq } = JSON.parse(response.body);
        console.log(`${sessionId} 세션에 연결합니다.`);
        navigate('/session', { state: { guideSeq, sessionId, token, invitation: false } });
      });
    },
    beforeDisconnected() {
      setClient(null);
    },
  });

  useEffect(() => {
    matchStart();
  });

  const matchStart = () => {
    if (client && !send) {
      getUserInfo({
        childId,
        onSuccess: data => {
          const userData: UserData = {
            childId,
            ...data,
          };
          const message = JSON.stringify(userData);
          client.publish({
            destination: `/app/match/start`,
            body: message,
          });
          setSend(true);
          console.log('메시지 전송: ', message);
        },
        onError: () => {
          console.log('프로필 정보를 가져오는데 실패했습니다.');
        },
      });
    }
  };

  return <Loading isAnimonLoaded />;
};

export default MatchPage;
