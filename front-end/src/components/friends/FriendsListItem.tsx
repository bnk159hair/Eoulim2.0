import React from 'react';
import axios from 'axios';
import { useRecoilValue, useRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { FriendCard, FriendImg, FrinedInfo, InviteButton } from './FriendsListItemStyles';
import { tokenState, userState } from '../../atoms/Auth';
import { invitationToken, invitationSessionId } from '../../atoms/Ivitation';
import { Profilekey } from '../../atoms/Profile';
import { API_BASE_URL } from '../../apis/urls';

interface FriendsListItemProps {
  friendId: number;
  friendName: string;
  animon: string;
  status: string;
}

const FriendsListItem: React.FC<FriendsListItemProps> = ({
  friendId,
  friendName,
  animon,
  status,
}) => {
  const [sessionToken, setSessionToken] = useRecoilState(invitationToken);
  const [invitationId, setInvitationId] = useRecoilState(invitationSessionId);

  const IMGURL = `/${animon}.png`;
  const navigate = useNavigate();

  const myName = useRecoilValue(userState);
  const token = useRecoilValue(tokenState);
  const profileKey = useRecoilValue(Profilekey);

  const handleInvite = () => {
    console.log(friendId, myName);
    axios
      .post(
        `${API_BASE_URL}/meetings/friend/start`,
        {
          childId: profileKey,
          friendId, // 친구 아이디
          name: myName, // 내 이름
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        },
      )
      .then(response => {
        const { sessionId, token } = response.data;
        setInvitationId(sessionId);
        setSessionToken(token);
        console.log(sessionId, token);

        navigate(`/friendsession`);
      })
      .catch(error => console.log(error));
  };

  return (
    <FriendCard>
      <FriendImg style={{ backgroundImage: `url(${IMGURL})` }} />
      <FrinedInfo>
        <div>
          {'친구 이름 : '}
          {friendName}
        </div>
        {status === 'ON' && <InviteButton onClick={handleInvite} />}
      </FrinedInfo>
    </FriendCard>
  );
};

export default FriendsListItem;
