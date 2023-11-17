import React from 'react';
import { useNavigate } from 'react-router-dom';
import FriendsList from '../../components/friends/FriendsList';
import { FriendsPageContainer, BackIcon } from './FriendsPageStyles';

const FriendsPage = () => {
  const navigate = useNavigate();

  const handleMainClick = () => {
    navigate('/');
  };

  return (
    <FriendsPageContainer>
      <BackIcon onClick={handleMainClick} />
      <FriendsList />
    </FriendsPageContainer>
  );
};

export default FriendsPage;
