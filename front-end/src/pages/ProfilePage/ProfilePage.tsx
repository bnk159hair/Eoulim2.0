import React, { useState } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { getLogout } from 'apis/authApis';
import { fcmTokenState } from 'atoms/Firebase';
import ProfileList from '../../components/profile/ProfileList';
import { ProfilePageContainer, PasswordChange, MarginContainer } from './ProfilePageStyles';
import ChangePasswordModal from '../../components/profile/ChangePasswordModal';
import { userState } from '../../atoms/Auth';

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isModalOpen, setModalOpen] = useState(false);
  const [, setUserName] = useRecoilState(userState);
  const fcmToken = useRecoilValue(fcmTokenState);

  const handleLogout = () => {
    let token;
    if (fcmToken) {
      token = fcmToken;
    } else {
      token = 'null';
    }
    getLogout({
      fcmToken: token,
      onSuccess: () => {
        navigate('/login');
      },
      onError: () => {
        setUserName('');
        navigate('/login');
      },
    });
  };

  return (
    <ProfilePageContainer>
      <MarginContainer>
        <PasswordChange
          onClick={() => {
            setModalOpen(true);
          }}
        />
      </MarginContainer>
      {isModalOpen && <ChangePasswordModal onClose={() => setModalOpen(false)} />}
      <ProfileList />
      <MarginContainer>
        <button onClick={handleLogout}>{'로그아웃'}</button>
      </MarginContainer>
    </ProfilePageContainer>
  );
};

export default ProfilePage;
