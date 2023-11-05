import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import { useRecoilState, useRecoilValue } from 'recoil';
import { Client } from '@stomp/stompjs';
import instance from 'apis/instance';
import Loading from '../../components/stream/Loading';
import { useOpenVidu } from '../../hooks/useOpenVidu';
import { StreamCanvas } from '../../components/stream/StreamCanvas';
import {
  Buttons,
  Character,
  CharacterContainer,
  Container,
  MyVideo,
  NavContainer,
  SessionPageContainer,
} from './SessionPageStyles';
import { Profile, Profilekey } from '../../atoms/Profile';
import { invitationToken, invitationSessionId } from '../../atoms/Ivitation';
import { tokenState } from '../../atoms/Auth';
import {
  PublisherId,
  SubscriberId,
  PublisherVideoStatus,
  SubscriberVideoStatus,
  PublisherAnimonURL,
  SubscriberAnimonURL,
  IsAnimonLoaded,
} from '../../atoms/Session';
import { WS_BASE_URL } from '../../apis/urls';
import { WebSocketApis } from '../../apis/webSocketApis';
import EndModal from '../../components/stream/EndModal';
import { destroySession } from '../../apis/openViduApis';

interface FriendsProfile {
  id: number;
  name: string;
  birth: number;
  gender: string;
  school: string;
  grade: number;
  status: string;
  animon: { id: number; imagePath: string; name: string };
}

const FriendSessionPage = () => {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [refuse, setRefuse] = useState(false);
  const [friends, setFriends] = useState<FriendsProfile[]>([]);
  const [isFriend, setFriend] = useState(false);
  const [publisherId, setPublisherId] = useRecoilState(PublisherId);
  const [subscriberId, setSubscriberId] = useRecoilState(SubscriberId);
  const [publisherVideoStatus, setPublisherVideoStatus] = useRecoilState(PublisherVideoStatus);
  const [subscriberVideoStatus, setSubscriberVideoStatus] = useRecoilState(SubscriberVideoStatus);
  const [publisherAnimonURL, setPublisherAnimonURL] = useRecoilState(PublisherAnimonURL);
  const [subscriberAnimonURL, setSubscriberAnimonURL] = useRecoilState(SubscriberAnimonURL);

  const sessionId = useRecoilValue(invitationSessionId);
  const sessionToken = useRecoilValue(invitationToken);

  const profileId = useRecoilValue(Profilekey);
  const userToken = useRecoilValue(tokenState);
  const profile = useRecoilValue(Profile);
  const isAnimonLoaded = useRecoilValue(IsAnimonLoaded);

  const [subscriberName, setSubscriberName] = useState('');

  console.log('오픈비두 시작');

  console.log(profileId, sessionId, sessionToken);
  const { streamList, session, isOpen, onChangeMicStatus } = useOpenVidu(
    profileId,
    sessionId,
    sessionToken,
  );

  const sessionOver = () => {
    setOpen(isTrue);
  };

  const [micStatus, setMicStatus] = useState(true);
  useEffect(() => {
    onChangeMicStatus(micStatus);
  }, [micStatus]);

  const [connected, setConnected] = useState<boolean>(false);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    setPublisherVideoStatus(isFalse);
    setSubscriberVideoStatus(isFalse);
    setPublisherId(profileId);
    setPublisherAnimonURL(`${profile.animon.name}mask.png`);
    console.log('친구불러오기 시작');
    getFriends();
  }, []);

  useEffect(() => {
    for (const user of streamList) {
      console.log('before', Number(user.userId), Number(publisherId));
      if (Number(user.userId) !== profileId) {
        console.log(user);
        console.log(user.userId, publisherId);
        setSubscriberId(user.userId);
        console.log(subscriberId);
      }
    }
    console.log(publisherId, subscriberId);
  }, [streamList]);

  useEffect(() => {
    setOpen(isOpen);
  }, [isOpen]);

  useEffect(() => {
    for (const user of streamList) {
      if (Number(user.userId) !== Number(publisherId)) {
        setSubscriberId(Number(user.userId));
        friends.forEach(user => {
          console.log(user.id, subscriberId);
          console.log(Number(user.id) === Number(subscriberId));
          if (String(user.id) === String(subscriberId)) {
            console.log('친구입니다.');
            setFriend(isTrue);
          }
        });
      }
    }
    console.log(publisherId, subscriberId);
  }, [subscriberId]);

  useEffect(() => {
    if (subscriberId) {
      getAnimon();
    }
  }, [subscriberId]);

  useEffect(() => {
    if (session) {
      const client = new Client({
        connectHeaders: WebSocketApis.getInstance().header,
        brokerURL: WS_BASE_URL,
        reconnectDelay: 5000,
        debug: str => console.log(str),
      });

      client.onConnect = () => {
        console.log('WebSocket 연결됨');
        setConnected(isTrue);
        setStompClient(client);

        client.subscribe(`/topic/${session.sessionId}/animon`, response => {
          console.log('메시지 수신:', response.body);
          const message = JSON.parse(response.body);
          if (String(message.childId) !== String(publisherId)) {
            console.log(message.childId, message.isAnimonOn);
            console.log('상대방이 화면을 껐습니다.');
            setSubscriberId(message.childId);
            setSubscriberVideoStatus(message.isAnimonOn);
          }
        });
        client.subscribe(`/topic/${session.sessionId}/leave-session`, response => {
          const message = JSON.parse(response.body);
          console.log(message);
          if (message.childId !== String(publisherId)) {
            if (message.isLeft === true) {
              setOpen(isTrue);
            } else if (message.isLeft === false) {
              console.log('초대를 거절했습니다.');
              setRefuse(isTrue);
              setOpen(isTrue);
            }
          }
        });
      };

      client.onDisconnect = () => {
        console.log('WebSocket 연결 닫힘');
        setConnected(isFalse);
        setStompClient(null);
      };

      client.activate();

      return () => {
        client.deactivate();
      };
    }
  }, [streamList]);

  const getFriends = async () => {
    console.log(profileId);
    try {
      const response = await instance.get(`/friendship/${profileId}`);
      setFriends(response.data.data);
      console.log(response);
    } catch (error) {
      console.log(error);
      throw error;
    }
    // instance
    //   .get(`/friendship/${profileId}`)
    //   .then(response => {
    //     const data = response.data.result;
    //     setFriends(data);
    //     console.log(data);
    //   })
    //   .catch(error => {
    //     if (error.response && error.response.status === 401) {
    //       navigate('/login');
    //     } else {
    //       console.log('친구목록불러오기오류', error);
    //     }
    //   });
  };

  const getAnimon = async () => {
    try {
      const response = await instance.get(`/children/participant/${subscriberId}`);
      console.log('유저 정보 가져오기 성공!');
      console.log(response);
      setSubscriberAnimonURL(`${response.data.result.animon.name}mask.png`);
      setSubscriberName(response.data.result.name);
      // return response.data.result;
    } catch (error) {
      console.log('유저 정보 가져오기 실패ㅠ');
      console.log(error);
      throw error;
    }
  };

  const leaveSession = () => {
    setRefuse(isFalse);
    setOpen(isFalse);
    if (connected && stompClient) {
      const jsonMessage = {
        childId: String(publisherId),
        isLeft: true,
      };
      const message = JSON.stringify(jsonMessage);
      stompClient.publish({
        destination: `/app/${session.sessionId}/leave-session`,
        body: message,
      });
      console.log('메시지 전송:', message);
    }
    destroySession(session, '', '');
    session.disconnect();
    navigate('/');
  };

  const addFriend = () => {
    console.log(publisherId, subscriberId);
    console.log(userToken);
    try {
      const response = instance.post(`/friendship`, {
        myId: Number(publisherId),
        friendId: Number(subscriberId),
      });
      console.log(response);
      leaveSession();
    } catch (error) {
      console.log(error);
      // if (error.response.data.resultCode === 'INVALID_DATA') {
      //   leaveSession();
      // } else console.log(error);
    }
    // instance
    //   .post(
    //     `/friendship`,
    //     { myId: Number(publisherId), friendId: Number(subscriberId) },

    //   )
    //   .then(response => {
    //     console.log(response);
    //     leaveSession();
    //   })
    //   .catch(error => {
    //     if (error.response.data.resultCode === 'INVALID_DATA') {
    //       leaveSession();
    //     } else console.log(error);
    //   });
  };

  const changeVideoStatus = () => {
    console.log(stompClient);
    if (connected && stompClient) {
      const isAnimonOn = !publisherVideoStatus;
      setPublisherVideoStatus(isAnimonOn);
      const jsonMessage = {
        childId: String(publisherId),
        isAnimonOn,
      };
      const message = JSON.stringify(jsonMessage);
      stompClient.publish({
        destination: `/app/${session.sessionId}/animon`,
        body: message,
      });
      console.log('메시지 전송:', message);
    }
  };

  const changeAudioStatus = () => {
    setMicStatus(prev => !prev);
  };

  const isTrue = () => {
    return true;
  };

  const isFalse = () => {
    return false;
  };

  return (
    // eslint-disable-next-line react/jsx-no-useless-fragment
    <>
      {!open ? (
        <SessionPageContainer>
          <Container>
            <MyVideo>
              {streamList.length > 1 && streamList[1].streamManager ? (
                <>
                  <StreamCanvas
                    streamManager={streamList[1].streamManager}
                    name={subscriberName}
                    avatarPath={subscriberAnimonURL}
                    videoState={subscriberVideoStatus}
                  />
                  <Loading isAnimonLoaded={isAnimonLoaded} />
                </>
              ) : (
                <Loading isAnimonLoaded={false} />
              )}
            </MyVideo>
            <CharacterContainer>
              <Character isPlaying={false} />
            </CharacterContainer>
            <MyVideo>
              {streamList.length > 1 && streamList[0].streamManager ? (
                <>
                  <StreamCanvas
                    streamManager={streamList[0].streamManager}
                    name={profile.name}
                    avatarPath={`${publisherAnimonURL}`}
                    videoState={publisherVideoStatus}
                  />
                  <Loading isAnimonLoaded={isAnimonLoaded} />
                </>
              ) : (
                <Loading isAnimonLoaded={false} />
              )}
            </MyVideo>
          </Container>
          <NavContainer>
            <Buttons>
              <Button variant={'contained'} onClick={changeVideoStatus} sx={{ fontSize: '30px' }}>
                {publisherVideoStatus ? (profile.gender === 'W' ? '👩' : '🧑') : '🙈'}
              </Button>
              <Button variant={'contained'} onClick={changeAudioStatus}>
                {micStatus ? <MicIcon fontSize={'large'} /> : <MicOffIcon fontSize={'large'} />}
              </Button>
              <Button
                variant={'contained'}
                color={'error'}
                onClick={sessionOver}
                sx={{ fontSize: '30px' }}
              >
                {'나가기'}
              </Button>
            </Buttons>
          </NavContainer>
        </SessionPageContainer>
      ) : streamList.length !== 2 ? (
        !refuse ? (
          leaveSession()
        ) : (
          <EndModal
            onClose={leaveSession}
            message={'친구가 지금 바쁜 상태입니다.'}
            isFriend
            addFriend={addFriend}
          />
        )
      ) : !isFriend ? (
        <EndModal
          onClose={leaveSession}
          message={'친구 조아?'}
          isFriend={isFriend}
          addFriend={addFriend}
        />
      ) : (
        <EndModal
          onClose={leaveSession}
          message={'통화가 끝났습니다.'}
          isFriend={isFriend}
          addFriend={addFriend}
        />
      )}
    </>
  );
};

export default FriendSessionPage;
