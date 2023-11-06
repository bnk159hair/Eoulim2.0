import React, { useState } from 'react';
import { useRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { getLogout } from 'apis/authApis';
import ProfileList from '../../components/profile/ProfileList';
import { ProfilePageContainer, PasswordChange, MarginContainer } from './ProfilePageStyles';
import ChangePasswordModal from '../../components/profile/ChangePasswordModal';
import { userState } from '../../atoms/Auth';

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isModalOpen, setModalOpen] = useState(false);
  const [, setUserName] = useRecoilState(userState);

  const handleLogout = () => {
    getLogout({
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
