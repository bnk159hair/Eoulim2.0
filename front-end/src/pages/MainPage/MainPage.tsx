import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import { childLogin, childLogout, getChildInfo } from 'apis/profileApis';
import { fcmTokenState } from 'atoms/Firebase';
import {
  MainPageContainer,
  ProfileImg,
  MarginContainer,
  MainCharacter,
  BackIcon,
  ChaterLocation,
  MyFriend,
  NewFriend,
  NewFirendsignpost,
  MyFirendsignpost,
  HoberLeft,
  HoberRight,
} from './MainPageStyles';
import { Profile, Profilekey } from '../../atoms/Profile';
import { tokenState } from '../../atoms/Auth';
import AnimonModal from '../../components/main/AnimonModal';
import { API_BASE_URL } from '../../apis/urls';
import AlarmModal from '../../components/main/AlarmModal';

const MainPage: React.FC = () => {
  const navigate = useNavigate();
  const profileId = useRecoilValue(Profilekey);
  const token = useRecoilValue(tokenState);
  const fcmToken = useRecoilValue(fcmTokenState);
  // const [profile, setProfile] = useRecoilState(Profile);
  const [_, setProfile] = useRecoilState(Profile);
  const [eventSource, setEventSource] = useState<EventSource | null>(null);
  const [sessionId, setSessionId] = useState<string>('');
  const [userName, setUserName] = useState<string>('');
  const childLoginoutData = { childId: profileId, fcmToken };

  useEffect(() => {
    const source = new EventSource(`${API_BASE_URL}/alarms/subscribe/${profileId}`);
    setEventSource(source);
    console.log(source, eventSource);
    return () => {
      if (source) {
        source.close();
        setEventSource(null);
        console.log('이벤트 종료');
      }
    };
  }, [navigate]);

  useEffect(() => {
    if (!token) {
      navigate('/login');
    } else {
      getChild();
      childLogin({
        childLoginData: childLoginoutData,
        onSuccess: () => {
          console.log('프로필 로그인에 성공했습니다.');
        },
        onError: () => {
          console.log('프로필 로그인에 실패하였습니다.');
        },
      });
    }
  }, [profileId, token, navigate]);

  useEffect(() => {
    if (eventSource) {
      const eventListener = (event: any) => {
        if (event.data === 'connect completed') {
          console.log('SSE와 연결');
        } else if (event) {
          console.log(event);
          const message = JSON.parse(event.data);
          console.log(message.sessionId);
          setSessionId(message.sessionId);
          setUserName(message.userName);
          setAlarmOpen(true);
        }
      };
      eventSource.addEventListener('sse', eventListener);

      return () => {
        eventSource.removeEventListener('sse', eventListener);
      };
    }
  });

  const getNewFriend = () => {
    navigate('/session');
    logout();
  };

  const handleFriendsClick = () => {
    navigate('/friends');
    logout();
  };
  const getBack = () => {
    logout();
    navigate('/profile');
  };

  const getChild = () => {
    getChildInfo({
      childId: profileId,
      onSuccess: data => {
        setProfile(data);
        console.log('프로필 가져오기에 성공하였습니다.');
      },
      onError: () => {
        console.log('프로필 가져오기에 실패하였습니다.');
      },
    });
  };

  const logout = () => {
    childLogout({
      childLogoutData: childLoginoutData,
      onSuccess: () => {
        console.log('프로필 로그아웃에 성공했습니다.');
      },
      onError: () => {
        console.log('프로필 로그아웃에 실패하였습니다.');
      },
    });
  };

  const openModal = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  const closeAlarm = () => {
    setAlarmOpen(false);
  };

  const [isModalOpen, setModalOpen] = useState(false);
  const [isAlarmOpen, setAlarmOpen] = useState(false);
  // const IMGURL = `/${profile.profileAnimon.name}.png`;
  const IMGURL = `/dog.png`;

  const audioObjRef = useRef(new Audio('/mainguide.mp3'));

  const playAudio = () => {
    const audioObj = audioObjRef.current;
    audioObj.currentTime = 0;
    audioObj.play();
  };

  useEffect(() => {
    return () => {
      audioObjRef.current.pause();
      audioObjRef.current.currentTime = 0;
    };
  }, []);

  return (
    <MainPageContainer>
      <MarginContainer>
        <BackIcon onClick={getBack} />
        <ProfileImg style={{ backgroundImage: `url(${IMGURL})` }} onClick={openModal} />
      </MarginContainer>
      <ChaterLocation>
        {isModalOpen && <AnimonModal onClose={closeModal} profile={getChild} />}
        {isAlarmOpen && (
          <AlarmModal onClose={closeAlarm} sessionId={sessionId} userName={userName} />
        )}
        <HoberLeft onClick={getNewFriend}>
          <NewFriend />
          <NewFirendsignpost />
        </HoberLeft>
        <MainCharacter style={{ backgroundImage: `url(${IMGURL})` }} onClick={playAudio} />
        <HoberRight onClick={handleFriendsClick}>
          <MyFirendsignpost />
          <MyFriend />
        </HoberRight>
      </ChaterLocation>
    </MainPageContainer>
  );
};

export default MainPage;
