import React, { useEffect, useState } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { postLogout } from 'apis/authApis';
import { fcmTokenState } from 'atoms/Firebase';
import { Box, Divider, Menu, MenuItem, Typography, Button } from '@mui/material';
import { deleteNotifications, getNotifications } from 'apis/notificationApis';
import formatTime from 'utils/formatTime';
import ProfileList from '../../components/profile/ProfileList';
import {
  ProfilePageContainer,
  PasswordChange,
  MarginContainer,
  Alarm,
  NotificationBadge,
} from './ProfilePageStyles';
import ChangePasswordModal from '../../components/profile/ChangePasswordModal';
import { userState } from '../../atoms/Auth';

interface Notification {
  createTime: number[];
  text: string;
}

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isModalOpen, setModalOpen] = useState(false);
  const [, setUserName] = useRecoilState(userState);
  const fcmToken = useRecoilValue(fcmTokenState);
  const [isReadNotifications, setIsReadNotifications] = useState(true);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const [notifications, setNotifications] = useState<Notification[]>([]);

  useEffect(() => {
    getNotifications({
      onSuccess: data => setNotifications(data.reverse()),
      onError: () => {},
    });
  }, []);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setIsReadNotifications(false);
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    let token;
    if (fcmToken) {
      token = fcmToken;
    } else {
      token = 'null';
    }
    postLogout({
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

  const handleDelete = () => {
    deleteNotifications();
    setNotifications([]);
  };

  return (
    <ProfilePageContainer>
      <MarginContainer>
        <Alarm onClick={handleClick}>
          {notifications.length === 0 ? (
            <NotificationBadge $isReadNotifications>{notifications.length}</NotificationBadge>
          ) : (
            <NotificationBadge $isReadNotifications={isReadNotifications}>
              {notifications.length}
            </NotificationBadge>
          )}
        </Alarm>
        {notifications.length > 0 && (
          <Menu
            MenuListProps={{
              'aria-labelledby': 'long-button',
            }}
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            sx={{
              maxHeight: 500,
              '*::-webkit-scrollbar': {
                display: 'none',
              },
              // textAlign: 'center',
              marginTop: 1,
            }}
            PaperProps={{
              style: { borderRadius: '15px' },
            }}
          >
            <Box sx={{ paddingX: 2.5, display: 'flex', justifyContent: 'space-between' }}>
              <Typography variant={'h5'} fontWeight={'bold'} padding={1}>
                {'알림 목록'}
              </Typography>
              <Button size={'large'} onClick={handleDelete}>
                {'목록 비우기'}
              </Button>
            </Box>
            {notifications.map(notification => (
              <Box
                key={notification.createTime.toString()}
                sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}
              >
                <Divider sx={{ my: 0.5, width: '90%' }} />
                <MenuItem
                  sx={{ width: 400, display: 'flex', flexDirection: 'column' }}
                  onClick={handleClose}
                >
                  <Typography variant={'body1'} fontWeight={'bold'}>
                    {notification.text}
                  </Typography>
                  <Typography variant={'body2'}>{`보낸 시각 : ${formatTime(
                    notification.createTime,
                  )}`}</Typography>
                </MenuItem>
              </Box>
            ))}
          </Menu>
        )}
        <PasswordChange onClick={() => setModalOpen(true)} />
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
