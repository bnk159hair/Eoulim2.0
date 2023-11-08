/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react';
import { Client, Frame } from '@stomp/stompjs';
import { WS_BASE_URL } from '../apis/urls';
import { WebSocketApis } from '../apis/webSocketApis';

interface Param {
  onConnect: (frame: Frame, client: Client) => void;
  beforeDisconnected: (frame: Frame, client: Client) => void;
  reconnectDelay?: number;
}

export const useWebSocket = (param: Param) => {
  const [connected, setConnected] = useState<boolean>(false);

  useEffect(() => {
    const client = new Client({
      connectHeaders: WebSocketApis.getInstance().header,
      brokerURL: WS_BASE_URL,
      // reconnectDelay: param.reconnectDelay ? param.reconnectDelay : 5000,
      debug: str => console.log(str),
    });

    client.onConnect = (frame: Frame) => {
      console.log('WebSocket 연결됨', frame);
      setConnected(true);
      param.onConnect(frame, client!);
    };

    client.onDisconnect = (frame: Frame) => {
      console.log('WebSocket 연결 닫힘');
      setConnected(false);
      param.beforeDisconnected(frame, client!);
    };

    client.activate();

    return () => {
      client?.deactivate();
    };
  }, []);

  return { connected };
};
