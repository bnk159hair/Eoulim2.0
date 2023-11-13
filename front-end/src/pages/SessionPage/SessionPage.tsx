import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button } from '@mui/material';
import { useRecoilValue, useRecoilState } from 'recoil';
import { Client } from '@stomp/stompjs';
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import { changeVideo, follow, getAnimon, getFriends } from 'apis/sessionApis';
import { useWebSocket } from 'hooks/useWebSocket';
import { InvitationSessionId, InvitationToken } from 'atoms/Ivitation';
import Loading from '../../components/stream/Loading';
import { useOpenVidu } from '../../hooks/useOpenVidu';
import { StreamCanvas } from '../../components/stream/StreamCanvas';
import {
  Buttons,
  Character,
  Container,
  MyVideo,
  NavContainer,
  SessionPageContainer,
  Click,
  CharacterContainer,
  CheckContainer,
  FriendVideo,
  CheckVideo,
} from './SessionPageStyles';
import { Profile } from '../../atoms/Profile';
import { IsAnimonLoaded, guideSeq, GuideScript, Timeline, SessionId } from '../../atoms/Session';
import EndModal from '../../components/stream/EndModal';
import { destroySession } from '../../apis/openViduApis';
import { S3_SOUND_BASE_URL } from '../../apis/urls';

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

const SessionPage = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { childId, invitation } = state;
  const [open, setOpen] = useState(false);
  const [refuse, setRefuse] = useState(false);
  const [first, setFirst] = useState(true);
  const [friends, setFriends] = useState<FriendsProfile[]>([]);
  const [isFriend, setFriend] = useState(false);

  const [friendId, setFriendId] = useState(0);
  const [publisherVideoStatus, setPublisherVideoStatus] = useState(false);
  const [subscriberVideoStatus, setSubscriberVideoStatus] = useState(false);
  const [publisherAnimonURL, setPublisherAnimonURL] = useState('');
  const [subscriberAnimonURL, setSubscriberAnimonURL] = useState('');
  const [publisherGuideStatus, setPublisherGuideStatus] = useState(false);
  const [subscriberGuideStatus, setSubscriberGuideStatus] = useState(false);

  const [clickEnabled, setClickEnabled] = useState(false);
  const profile = useRecoilValue(Profile);
  const [subscriberName, setSubscriberName] = useState('');
  const isAnimonLoaded = useRecoilValue(IsAnimonLoaded);
  const step = useRecoilValue(guideSeq);
  const [index, setIndex] = useState(-1);
  const guideSequence = [...step, 13];
  const guidance = new Audio(`${S3_SOUND_BASE_URL}/guide/1.mp3`);
  const [isPlaying, setIsPlaying] = useState(false);

  const [guideScript, setGuideScript] = useRecoilState(GuideScript);
  const [timeline, setTimeline] = useRecoilState(Timeline);
  const [startTime, setStartTime] = useState(0);

  const sessionId = useRecoilValue(SessionId);
  const invitationSessionId = useRecoilValue(InvitationSessionId);
  const sessionToken = useRecoilValue(InvitationToken);

  const { streamList, session, isOpen, onChangeMicStatus } = useOpenVidu(
    childId,
    invitationSessionId,
    sessionToken,
  );

  const [micStatus, setMicStatus] = useState(true);

  useEffect(() => {
    onChangeMicStatus(micStatus);
  }, [micStatus]);

  const sessionOver = () => {
    setOpen(isTrue);
  };

  const [client, setClient] = useState<Client | null>(null);

  useEffect(() => {
    setGuideScript([]);
    setTimeline([]);
    setPublisherAnimonURL(`${profile.profileAnimon.maskImagePath}`);
    getFriends({
      childId,
      onSuccess: data => {
        setFriends(data);
        console.log('친구 불러오기 성공');
      },
      onError: () => {
        console.log('친구 불러오기 실패');
      },
    });
  }, []);

  useEffect(() => {
    for (const user of streamList) {
      console.log('before', Number(user.userId), Number(childId));
      if (Number(user.userId) !== childId) {
        console.log(user.userId, childId);
        setFriendId(Number(user.userId));
      }
    }
    console.log(childId, friendId);

    if (!state.invitation && !open && streamList[0]?.userId && streamList[1]?.userId && first) {
      setFirst(isFalse);
      setStartTime(Date.now());
      setTimeout(() => {
        guidance.play();
        setIsPlaying(true);
      }, 5000);
      guidance.addEventListener('ended', () => {
        setIsPlaying(false);
      });
    }
  }, [streamList]);

  useEffect(() => {
    setOpen(isOpen);
  }, [isOpen]);

  useEffect(() => {
    for (const user of streamList) {
      if (Number(user.userId) !== Number(childId)) {
        setFriendId(Number(user.userId));
        friends.forEach(user => {
          console.log(user.id, friendId);
          console.log(Number(user.id) === Number(friendId));
          if (String(user.id) === String(friendId)) {
            console.log('친구입니다.');
            setFriend(isTrue);
          }
        });
      }
    }
    console.log(childId, friendId);
  }, [friendId]);

  useEffect(() => {
    if (friendId) {
      getAnimon({
        friendId,
        onSuccess: data => {
          setSubscriberAnimonURL(`${data.profileAnimon.maskImagePath}`);
          setSubscriberName(data.name);
        },
        onError: () => {
          console.log('상대방의 애니몬을 불러올 수 없습니다.');
        },
      });
    }
  }, [friendId]);

  useEffect(() => {
    if (!state.invitation && publisherGuideStatus && subscriberGuideStatus) {
      const nextIndex = index + 1;
      setIndex(nextIndex);
      const guidance = new Audio(`${S3_SOUND_BASE_URL}/guide/${guideSequence[nextIndex]}.mp3`);
      if (nextIndex <= 4) {
        setGuideScript([...guideScript, guideSequence[nextIndex]]);
        setTimeline([...timeline, String(Date.now() - startTime)]);
        console.log(guideScript, timeline);
        guidance.play();
      }
      setIsPlaying(true);
      setPublisherGuideStatus(isFalse);
      setSubscriberGuideStatus(isFalse);
      guidance.addEventListener('ended', () => {
        setIsPlaying(false);
        if (nextIndex === 4) {
          console.log(guideScript, timeline);
          sessionOver();
        }
      });
      setTimeout(() => {
        setClickEnabled(true);
      }, 30000);
    }
  }, [publisherGuideStatus, subscriberGuideStatus]);

  useWebSocket({
    onConnect(_, client) {
      console.log('++++++++++++++++++++++++++++++++++++++', sessionId);
      setClient(client);
      client.subscribe(`/topic/${sessionId}/animon`, response => {
        console.log('메시지 수신:', response.body);
        const message = JSON.parse(response.body);
        if (message.childId !== String(childId)) {
          console.log(message.childId, message.isAnimonOn);
          console.log('상대방이 화면을 껐습니다.');
          // setFriendId(message.childId);
          setSubscriberVideoStatus(message.isAnimonOn);
        }
      });
      if (!state.invitation) {
        client.subscribe(`/topic/${sessionId}/guide`, response => {
          const message = JSON.parse(response.body);
          console.log(message);
          if (message.childId !== String(childId)) {
            // setFriendId(message.childId);
            setSubscriberGuideStatus(message.isNextGuideOn);
          }
        });
      }
      client.subscribe(`/topic/${sessionId}/leave-session`, response => {
        const message = JSON.parse(response.body);
        console.log(message);
        if (message.childId !== String(childId)) {
          setOpen(isTrue);
        }
      });
    },
    beforeDisconnected() {
      // setClient(null);
    },
  });

  useEffect(() => {
    const timer = setTimeout(() => {
      setClickEnabled(true);
    }, 30000);
    return () => {
      clearTimeout(timer);
    };
  }, []);

  const leaveSession = () => {
    setOpen(false);
    if (invitation) {
      if (client) {
        const jsonMessage = {
          childId: String(childId),
          isLeft: true,
        };
        const message = JSON.stringify(jsonMessage);
        client.publish({
          destination: `/app/${sessionId}/leave-session`,
          body: message,
        });
        console.log('메시지 전송:', message);
      }
      const sessionData = { sessionId, guideScript, timeline };
      destroySession({
        sessionData,
        onSuccess: () => {
          console.log(guideScript, timeline);
          console.log('세션 페이지에서 세션 접속을 종료하였습니다.');
        },
        onError: () => {
          console.log('세션 페이지에서 세션 접속 종료에 실패했습니다.');
        },
      });
    } else {
      setRefuse(true);
    }
    session.disconnect();
    navigate('/');
  };

  const addFriend = () => {
    console.log(childId, friendId);
    const followingData = { childId, followingChildId: friendId };
    follow({
      followingData,
      onSuccess: () => {
        leaveSession();
      },
      onError: () => {
        console.log('친구 추가를 실패하였습니다.');
      },
    });
  };

  const changeVideoStatus = () => {
    const videoData = { id: childId, status: publisherVideoStatus };
    changeVideo({
      videoData,
      onSuccess: (isAnimonOn, message) => {
        console.log(sessionId);
        setPublisherVideoStatus(isAnimonOn);
        client?.publish({
          destination: `/app/${sessionId}/animon`,
          body: message,
        });
      },
      onError: () => {
        console.log('애니몬 전환에 실패하였습니다.');
      },
    });
  };

  const changeAudioStatus = () => {
    setMicStatus(prev => !prev);
  };

  const nextGuidance = () => {
    if (!state.invitation && clickEnabled) {
      setClickEnabled(false); // 클릭 비활성화
      if (client) {
        const isNextGuideOn = !publisherGuideStatus;
        setPublisherGuideStatus(isNextGuideOn);
        const jsonMessage = {
          childId: String(childId),
          isNextGuideOn,
        };
        const message = JSON.stringify(jsonMessage);
        client.publish({
          destination: `/app/${sessionId}/guide`,
          body: message,
        });
        console.log('가이드 전송:', message);
      }
    }
  };

  const isTrue = () => {
    return true;
  };

  const isFalse = () => {
    return false;
  };

  const [checkVideo, setCheckVideo] = useState(false);

  if (open) {
    if (streamList.length !== 2) {
      if (!refuse) {
        leaveSession();
        return null;
      }
      return (
        <EndModal
          onClose={leaveSession}
          message={'친구가 지금 바쁜 상태입니다.'}
          isFriend
          addFriend={addFriend}
        />
      );
    }
    if (!isFriend) {
      return (
        <EndModal
          onClose={leaveSession}
          message={'친구 조아?'}
          isFriend={isFriend}
          addFriend={addFriend}
        />
      );
    }
    return (
      <EndModal
        onClose={leaveSession}
        message={'통화가 끝났습니다.'}
        isFriend={isFriend}
        addFriend={addFriend}
      />
    );
  }
  if (!checkVideo) {
    return (
      <SessionPageContainer>
        <CheckContainer>
          <CheckVideo>
            {streamList[0]?.streamManager && (
              <StreamCanvas
                streamManager={streamList[0]?.streamManager}
                name={subscriberName}
                avatarPath={subscriberAnimonURL}
                videoState={subscriberVideoStatus}
              />
            )}
          </CheckVideo>
          <button onClick={() => setCheckVideo(true)}>{'체크완료'}</button>
        </CheckContainer>
      </SessionPageContainer>
    );
  }
  return (
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

        {!invitation ? (
          <CharacterContainer>
            <Character onClick={nextGuidance} isPlaying={isPlaying}>
              {clickEnabled ? <Click /> : null}
            </Character>
          </CharacterContainer>
        ) : null}
        <FriendVideo>
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
        </FriendVideo>
      </Container>
      <NavContainer>
        <Buttons>
          <Button variant={'contained'} onClick={changeVideoStatus} sx={{ fontSize: '28px' }}>
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
  );
};

export default SessionPage;
