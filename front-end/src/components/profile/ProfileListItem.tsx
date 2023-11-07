import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useRecoilValue, useRecoilState } from 'recoil';
import { Button } from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { ProfileUserContainer, NameTag, ButtonContainer } from './ProfileListItemStyles';
import ModifyModal from './ModifyModal';
import { API_BASE_URL } from '../../apis/urls';
import { tokenState, userState } from '../../atoms/Auth';
import { Profilekey } from '../../atoms/Profile';
import ToRecordModal from './ToRecordModal';

const theme = createTheme({
  palette: {
    primary: {
      main: '#b6d36f',
    },
  },
});

interface ProfileListItemProps {
  name: string;
  childId: number;
  getChildren: () => void;
  imgurl: string;
}

const ProfileListItem: React.FC<ProfileListItemProps> = ({
  name,
  childId,
  getChildren,
  imgurl,
}) => {
  const [isModalOpen, setModalOpen] = useState(false);
  const [isRecordOpen, setIsRecordOpen] = useState(false);
  const token = useRecoilValue(tokenState);
  const [, setProfileKey] = useRecoilState(Profilekey);
  const [, setUserName] = useRecoilState(userState);
  const navigate = useNavigate();
  const IMGURL = imgurl;

  const handleModalOpen = () => {
    setModalOpen(true);
  };

  const handleModalClose = () => {
    setModalOpen(false);
  };

  const handleMainClick = () => {
    console.log(childId, name);
    setProfileKey(childId);
    setUserName(name);
    profileLogin();
    navigate('/');
  };

  const profileLogin = () => {
    axios
      .get(
        `${API_BASE_URL}/children/${childId}`,

        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )
      .then(() => {
        console.log('프로필로그인');
      })
      .catch(error => {
        console.log('프로필 로그인 오류', error);
      });
  };

  const handleRecordOpen = () => {
    setIsRecordOpen(true);
  };

  const handleRecordClose = () => {
    setIsRecordOpen(false);
  };

  return (
    <ThemeProvider theme={theme}>
      <div>
        <ProfileUserContainer
          style={{ backgroundImage: `url(${IMGURL})` }}
          onClick={handleMainClick}
        >
          <NameTag>{name}</NameTag>
        </ProfileUserContainer>
        <ButtonContainer>
          <Button
            variant={'contained'}
            sx={{ fontSize: '18px', paddingX: '1.8rem' }}
            onClick={handleModalOpen}
          >
            {'프로필 관리'}
          </Button>
          <Button
            variant={'contained'}
            sx={{ fontSize: '18px', paddingX: '1.8rem' }}
            onClick={handleRecordOpen}
          >
            {'녹화영상'}
          </Button>
        </ButtonContainer>
      </div>
      {isModalOpen && (
        <ModifyModal onClose={handleModalClose} childId={childId} getChildren={getChildren} />
      )}
      {isRecordOpen && <ToRecordModal onClose={handleRecordClose} childId={childId} />}
    </ThemeProvider>
  );
};

export default ProfileListItem;
