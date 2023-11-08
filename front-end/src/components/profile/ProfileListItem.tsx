import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue, useRecoilState } from 'recoil';
import { Button } from '@mui/material';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { fcmTokenState } from 'atoms/Firebase';
import { childLogin } from 'apis/profileApis';
import { ProfileUserContainer, NameTag, ButtonContainer } from './ProfileListItemStyles';
import ModifyModal from './ModifyModal';
import { userState } from '../../atoms/Auth';
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
  imgUrl: string;
}

const ProfileListItem: React.FC<ProfileListItemProps> = ({
  name,
  childId,
  getChildren,
  imgUrl,
}) => {
  const navigate = useNavigate();
  const [isModalOpen, setModalOpen] = useState(false);
  const [isRecordOpen, setIsRecordOpen] = useState(false);
  const [, setChildId] = useRecoilState(Profilekey);
  const fcmToken = useRecoilValue(fcmTokenState);
  const [, setUserName] = useRecoilState(userState);

  const handleChildLogin = () => {
    console.log(fcmToken);
    let childLoginoutData;
    if (fcmToken) {
      childLoginoutData = { childId, fcmToken };
    } else {
      childLoginoutData = { childId, fcmToken: 'null' };
    }

    childLogin({
      childLoginData: childLoginoutData,
      onSuccess: () => {
        console.log('프로필 로그인에 성공했습니다.');
        setChildId(childId);
        setUserName(name);
      },
      onError: () => {
        console.log('프로필 로그인에 실패하였습니다.');
      },
    });
    navigate('/');
  };

  return (
    <ThemeProvider theme={theme}>
      <div>
        <ProfileUserContainer
          style={{ backgroundImage: `url(${imgUrl})` }}
          onClick={handleChildLogin}
        >
          <NameTag>{name}</NameTag>
        </ProfileUserContainer>
        <ButtonContainer>
          <Button
            variant={'contained'}
            sx={{ fontSize: '18px', paddingX: '1.8rem' }}
            onClick={() => setModalOpen(true)}
          >
            {'프로필 관리'}
          </Button>
          <Button
            variant={'contained'}
            sx={{ fontSize: '18px', paddingX: '1.8rem' }}
            onClick={() => setIsRecordOpen(true)}
          >
            {'녹화영상'}
          </Button>
        </ButtonContainer>
      </div>
      {isModalOpen && (
        <ModifyModal onClose={() => setModalOpen(false)} id={childId} getChildren={getChildren} />
      )}
      {isRecordOpen && <ToRecordModal onClose={() => setIsRecordOpen(false)} childId={childId} />}
    </ThemeProvider>
  );
};

export default ProfileListItem;
