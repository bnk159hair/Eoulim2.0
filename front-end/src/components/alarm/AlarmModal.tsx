import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import { Client } from '@stomp/stompjs';
import {
  ModalOverlay,
  ModalContent,
  Accept,
  Refuse,
  FlexContent,
  AlarmMessage,
} from './AlarmModalStyles';
import { Profilekey } from '../../atoms/Profile';
import { InvitationSessionId } from '../../atoms/Ivitation';
import { WebSocketApis } from '../../apis/webSocketApis';
import { WS_BASE_URL } from '../../apis/urls';

interface AlarmModalProps {
  sessionId: string;
  userName: string;
  onClose: () => void;
}

const AlarmModal: React.FC<AlarmModalProps> = ({ sessionId, onClose, userName }) => {
  const navigate = useNavigate();

  const [, setInvitationId] = useRecoilState(InvitationSessionId);
  const childId = useRecoilValue(Profilekey);

  const [connected, setConnected] = useState<boolean>(false);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const client = new Client({
      connectHeaders: WebSocketApis.getInstance().header,
      brokerURL: WS_BASE_URL,
      reconnectDelay: 5000,
      debug: str => console.log(str),
    });
    console.log(client);
    client.onConnect = () => {
      console.log('WebSocket 연결됨');
      setConnected(true);
      setStompClient(client);

      client.subscribe(`/topic/${sessionId}/leave-session`, response => {
        const message = JSON.parse(response.body);
        console.log(message);
      });
    };

    client.onDisconnect = () => {
      console.log('WebSocket 연결 닫힘');
      setConnected(false);
      setStompClient(null);
    };

    client.activate();
    return () => {
      client.deactivate();
    };
  }, []);

  const acceptInvitation = () => {
    setInvitationId(sessionId);
    navigate(`/session`, { state: { childId, invitation: true } });
  };

  const refuseInvitaion = () => {
    if (connected && stompClient) {
      const jsonMessage = {
        childId: String(childId),
        isLeft: false,
      };
      const message = JSON.stringify(jsonMessage);
      stompClient.publish({
        destination: `/app/${sessionId}/leave-session`,
        body: message,
      });
      console.log('메시지 전송:', message);
      stompClient.deactivate();
    }
    onClose();
  };

  return (
    <ModalOverlay onClick={onClose}>
      <ModalContent>
        <AlarmMessage>
          {userName}
          {'님이 초대를 보내셨습니다'}
        </AlarmMessage>
        <FlexContent>
          <Accept onClick={acceptInvitation} />
          <Refuse onClick={refuseInvitaion} />
        </FlexContent>
      </ModalContent>
    </ModalOverlay>
  );
};

export default AlarmModal;
