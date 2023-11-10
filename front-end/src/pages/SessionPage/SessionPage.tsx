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
} from './SessionPageStyles';
import { Profile, Profilekey } from '../../atoms/Profile';
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
  const { invitation } = state;
  const [open, setOpen] = useState(false);
  const [first, setFirst] = useState(true);
  const [friends, setFriends] = useState<FriendsProfile[]>([]);
  const [isFriend, setFriend] = useState(false);

  const publisherId = useRecoilValue(Profilekey);
  const [subscriberId, setSubscriberId] = useState(0);
  const [publisherVideoStatus, setPublisherVideoStatus] = useState(false);
  const [subscriberVideoStatus, setSubscriberVideoStatus] = useState(false);
  const [publisherAnimonURL, setPublisherAnimonURL] = useState('');
  const [subscriberAnimonURL, setSubscriberAnimonURL] = useState('');
  const [publisherGuideStatus, setPublisherGuideStatus] = useState(false);
  const [subscriberGuideStatus, setSubscriberGuideStatus] = useState(false);

  const [clickEnabled, setClickEnabled] = useState(false);
  const [profileId] = useRecoilState(Profilekey);
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
    profileId,
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
      profileId,
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
      console.log('before', Number(user.userId), Number(publisherId));
      if (Number(user.userId) !== Number(profileId)) {
        console.log(user.userId, publisherId);
        setSubscriberId(Number(user.userId));
      }
    }
    console.log(publisherId, subscriberId);

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
      getAnimon({
        subscriberId,
        onSuccess: data => {
          setSubscriberAnimonURL(`${data.profileAnimon.maskImagePath}`);
          setSubscriberName(data.name);
        },
        onError: () => {
          console.log('상대방의 애니몬을 불러올 수 없습니다.');
        },
      });
    }
  }, [subscriberId]);

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
        if (message.childId !== String(publisherId)) {
          console.log(message.childId, message.isAnimonOn);
          console.log('상대방이 화면을 껐습니다.');
          // setSubscriberId(message.childId);
          setSubscriberVideoStatus(message.isAnimonOn);
        }
      });
      if (!state.invitation) {
        client.subscribe(`/topic/${sessionId}/guide`, response => {
          const message = JSON.parse(response.body);
          console.log(message);
          if (message.childId !== String(publisherId)) {
            // setSubscriberId(message.childId);
            setSubscriberGuideStatus(message.isNextGuideOn);
          }
        });
      }
      client.subscribe(`/topic/${sessionId}/leave-session`, response => {
        const message = JSON.parse(response.body);
        console.log(message);
        if (message.childId !== String(publisherId)) {
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
    if (client) {
      const jsonMessage = {
        childId: String(publisherId),
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
    session.disconnect();
    navigate('/');
  };

  const addFriend = () => {
    console.log(publisherId, subscriberId);
    const followingData = { childId: publisherId, followingChildId: subscriberId };
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
    const videoData = { id: publisherId, status: publisherVideoStatus };
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
          childId: String(publisherId),
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
      leaveSession();
      return null;
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
        <Container>
          <MyVideo>
            {streamList[1]?.streamManager && (
              <StreamCanvas
                streamManager={streamList[1]?.streamManager}
                name={subscriberName}
                avatarPath={subscriberAnimonURL}
                videoState={subscriberVideoStatus}
              />
            )}
          </MyVideo>
          <button onClick={() => setCheckVideo(true)}>{'체크완료'}</button>
        </Container>
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
