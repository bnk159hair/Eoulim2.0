import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import { childLogout, getChildInfo } from 'apis/profileApis';
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
import AlarmModal from '../../components/main/AlarmModal';
import { S3_SOUND_BASE_URL } from '../../apis/urls';

const MainPage: React.FC = () => {
  const navigate = useNavigate();
  const profileId = useRecoilValue(Profilekey);
  const token = useRecoilValue(tokenState);
  const fcmToken = useRecoilValue(fcmTokenState);
  const [profile, setProfile] = useRecoilState(Profile);
  const [sessionId] = useState<string>('');
  const [userName] = useState<string>('');

  useEffect(() => {
    getChild();
  }, [profileId, token, navigate]);

  const getNewFriend = () => {
    navigate('/session', { state: { invitation: false } });
  };

  const handleFriendsClick = () => {
    navigate('/friends');
  };
  const getBack = () => {
    logout();
    navigate('/profile');
  };

  const getChild = () => {
    getChildInfo({
      id: profileId,
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
    let childLoginoutData;

    if (fcmToken) {
      childLoginoutData = { childId: profileId, fcmToken };
    } else {
      childLoginoutData = { childId: profileId, fcmToken: 'null' };
    }

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
  const IMGURL = `/${profile.profileAnimon.name}.png`;
  // const IMGURL = `/dog.png`;

  const audioObjRef = useRef(new Audio(`${S3_SOUND_BASE_URL}/guide/main.mp3`));

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
