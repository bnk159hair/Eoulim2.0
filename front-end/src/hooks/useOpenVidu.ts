import { OpenVidu } from 'openvidu-browser';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useRecoilState } from 'recoil';
import { getUserInfo, getToken, invite, destroyInvitationSession } from '../apis/openViduApis';
import { SessionId, guideSeq } from '../atoms/Session';
import { InvitationSessionId, InvitationToken } from '../atoms/Ivitation';

export const useOpenVidu = (userId: number, sessionId: string, sessionToken: string) => {
  const [session, setSession] = useState<any>(null);
  const [publisher, setPublisher] = useState<any>(null);
  const [subscribers, setSubscribers] = useState<any[]>([]);
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [, setGuide] = useRecoilState(guideSeq);

  const [, setSessionId] = useRecoilState(SessionId);
  const [, setInvitationSessionId] = useRecoilState(InvitationSessionId);
  const [, setSessionToken] = useRecoilState(InvitationToken);

  console.log('session, publisher, subscribers 생성');

  const leaveSession = useCallback(() => {
    console.log('나가기 실행');
    if (sessionId) {
      console.log('초대 세션이랑 연결 끊기');
      destroyInvitationSession({
        sessionId,
        onSuccess: () => {
          setInvitationSessionId('');
          setSessionToken('');
          session.disconnect();
        },
        onError: () => {
          console.log('destroyInvitationSession api를 사용한 세션 접속 종료에 실패하였습니다.');
        },
      });
    } else if (session) {
      console.log('나랑 세션이랑 연결 끊기');
      session.disconnect();
    }
    setSession(null);
    setPublisher(null);
    setSubscribers([]);
  }, [session]);

  console.log('내 아이디 : ', userId);

  useEffect(() => {
    console.log('새로운 OV 객체 생성');
    const OV = new OpenVidu();
    //   OV.enableProdMode(); // 배포 시 사용 production 모드로 전환
    console.log('세션 시작');
    const mySession = OV.initSession();

    mySession.on('streamCreated', event => {
      console.log('스트림 생성');
      const subscriber = mySession.subscribe(event.stream, '');
      const data = JSON.parse(event.stream.connection.data);
      console.log(data);
      setSubscribers(prev => {
        return [
          ...prev.filter(sub => sub.userId !== data.childId),
          {
            streamManager: subscriber,
            userId: Number(data.childId),
          },
        ];
      });
    });
    // mySession.on('streamDestroyed', () => leaveSession());
    mySession.on('streamDestroyed', () => {
      if (session) {
        leaveSession();
      } else {
        setIsOpen(true);
        return {
          publisher,
          streamList,
          session,
          isOpen,
          onChangeCameraStatus,
          onChangeMicStatus,
        };
      }
    });
    // mySession.on('exception', (exception) => console.warn(exception));

    if (sessionId === '') {
      getUserInfo({
        childId: userId,
        onSuccess: data => {
          const userData = {
            childId: userId,
            ...data,
          };
          getToken({
            userData,
            onSuccess: data => {
              const { token, guideSeq, sessionId } = data;
              setSessionId(sessionId);
              setGuide(guideSeq);
              console.log(`가져온 토큰 ${token}으로 세션에 연결`);
              mySession
                .connect(token, { childId: String(userId) })
                .then(async () => {
                  await navigator.mediaDevices.getUserMedia({
                    audio: true,
                    video: true,
                  });
                  const devices = await OV.getDevices();
                  console.log(devices);
                  const videoDevices = devices.filter(device => device.kind === 'videoinput');

                  console.log('나를 publisher라고 하자!');
                  const publisher = OV.initPublisher('', {
                    audioSource: undefined,
                    videoSource: videoDevices[0].deviceId,
                    publishAudio: true,
                    publishVideo: true,
                    resolution: '640x480',
                    frameRate: 30,
                    insertMode: 'APPEND',
                    mirror: false,
                  });
                  console.log('publisher의 옵션을 설정했고 세션 연결을 성공했다!');
                  setPublisher(publisher);
                  mySession.publish(publisher);
                })
                .catch(error => {
                  console.log('세션 연결을 실패했다!');
                  console.log(
                    'There was an error connecting to the session:',
                    error.code,
                    error.message,
                  );
                });
            },
            onError: () => {
              console.log('토큰을 가져오는데 실패했습니다.');
            },
          });
        },
        onError: () => {
          console.log('유저 정보를 가져오는데 실패했습니다.');
        },
      });
    } else if (userId && sessionId && sessionToken) {
      mySession
        .connect(sessionToken, { childId: String(userId) })
        .then(async () => {
          await navigator.mediaDevices.getUserMedia({
            audio: true,
            video: true,
          });
          const devices = await OV.getDevices();
          const videoDevices = devices.filter(device => device.kind === 'videoinput');

          console.log('나를 publisher라고 하자!');
          const publisher = OV.initPublisher('', {
            audioSource: undefined,
            videoSource: videoDevices[0].deviceId,
            publishAudio: true,
            publishVideo: true,
            resolution: '640x480',
            frameRate: 30,
            insertMode: 'APPEND',
            mirror: false,
          });
          console.log('publisher의 옵션을 설정했고 초대 세션 연결을 성공했다!');
          setPublisher(publisher);
          mySession.publish(publisher);
        })
        .catch(error => {
          console.log('초대 세션 연결을 실패했다!');
          console.log('There was an error connecting to the session:', error.code, error.message);
        });
    } else if (userId && sessionId && sessionToken === '') {
      const friendId = Number(sessionId.split('_')[0]);
      const invitationSessionData = { childId: userId, sessionId, friendId };
      invite({
        invitationSessionData,
        onSuccess: data => {
          const { token, sessionId } = data;
          setSessionId(sessionId);
          mySession
            .connect(token, { childId: String(userId) })
            .then(async () => {
              await navigator.mediaDevices.getUserMedia({
                audio: true,
                video: true,
              });
              const devices = await OV.getDevices();
              const videoDevices = devices.filter(device => device.kind === 'videoinput');

              console.log('나를 publisher라고 하자!');
              const publisher = OV.initPublisher('', {
                audioSource: undefined,
                videoSource: videoDevices[0].deviceId,
                publishAudio: true,
                publishVideo: true,
                resolution: '640x480',
                frameRate: 30,
                insertMode: 'APPEND',
                mirror: false,
              });
              console.log('publisher의 옵션을 설정했고 초대 세션 연결을 성공했다!');
              setPublisher(publisher);
              mySession.publish(publisher);
            })
            .catch(error => {
              console.log('초대 세션 연결을 실패했다!');
              console.log(
                'There was an error connecting to the session:',
                error.code,
                error.message,
              );
            });
        },
        onError: () => {
          console.log('초대 세션 토큰 발급에 실패했습니다.');
        },
      });
    }

    setSession(mySession);
    console.log(mySession);
    return () => {
      console.log('useEffect가 return했다!!');

      if (sessionId) {
        console.log('초대 세션이랑 연결 끊기');
        destroyInvitationSession({
          sessionId,
          onSuccess: () => {
            setInvitationSessionId('');
            setSessionToken('');
          },
          onError: () => {
            console.log('destroyInvitationSession api를 사용한 세션 접속 종료에 실패하였습니다.');
          },
        });
      } else if (mySession) {
        console.log('서버에 세션 끊어달라고 보내기');
        console.log(mySession);
      }
      setSession(null);
      setPublisher(null);
      setSubscribers([]);
    };
  }, []);

  useEffect(() => {
    console.log('탭 종료 시에 leaveSession 함수 실행할 것이다.');
    const beforeUnloadHandler = () => leaveSession();
    window.addEventListener('beforeunload', beforeUnloadHandler);

    return () => {
      console.log('탭이 종료되었다.');
      window.removeEventListener('beforeunload', beforeUnloadHandler);
    };
  }, []);

  const onChangeCameraStatus = useCallback(
    (status: boolean) => {
      publisher?.publishVideo(status);
    },
    [publisher],
  );

  const onChangeMicStatus = useCallback(
    (status: boolean) => {
      publisher?.publishAudio(status);
    },
    [publisher],
  );

  const streamList = useMemo(
    () => [{ streamManager: publisher, userId }, ...subscribers],
    [publisher, subscribers, userId],
  );
  console.log(streamList);
  return {
    publisher,
    streamList,
    session,
    isOpen,
    onChangeCameraStatus,
    onChangeMicStatus,
  };
};
