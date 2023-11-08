import React from 'react';
import { useRecoilValue, useRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { getInvitationToken } from 'apis/openViduApis';
import { FriendCard, FriendImg, FrinedInfo, InviteButton } from './FriendsListItemStyles';
import { invitationToken, invitationSessionId } from '../../atoms/Ivitation';
import { Profilekey } from '../../atoms/Profile';

interface FriendsListItemProps {
  friendId: number;
  friendName: string;
  animon: string;
}

const FriendsListItem: React.FC<FriendsListItemProps> = ({ friendId, friendName, animon }) => {
  const [, setSessionToken] = useRecoilState(invitationToken);
  const [, setInvitationId] = useRecoilState(invitationSessionId);

  const IMGURL = `/${animon}.png`;
  const navigate = useNavigate();

  const childId = useRecoilValue(Profilekey);

  const handleInvite = () => {
    const invitationSessionData = { childId, friendId };
    getInvitationToken({
      invitationSessionData,
      onSuccess: data => {
        const { sessionId, token } = data;
        setInvitationId(sessionId);
        setSessionToken(token);
        console.log('초대에 보내는데 성공했습니다.');
        navigate(`/session`, { state: { invitation: true } });
      },
      onError: () => {
        console.log('초대에 실패하였습니다.');
      },
    });
  };

  return (
    <FriendCard>
      <FriendImg style={{ backgroundImage: `url(${IMGURL})` }} />
      <FrinedInfo>
        <div>
          {'친구 이름 : '}
          {friendName}
        </div>
        <InviteButton onClick={handleInvite} />
      </FrinedInfo>
    </FriendCard>
  );
};

export default FriendsListItem;
