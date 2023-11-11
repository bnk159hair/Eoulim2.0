import React from 'react';
import { useRecoilValue, useRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import { invite } from 'apis/openViduApis';
import inputAlert from 'utils/inputAlert';
import { FriendCard, FriendImg, FrinedInfo, InviteButton } from './FriendsListItemStyles';
import { InvitationToken, InvitationSessionId } from '../../atoms/Ivitation';
import { Profilekey } from '../../atoms/Profile';

interface FriendsListItemProps {
  friendId: number;
  friendName: string;
  animonImgPath: string;
}

const FriendsListItem: React.FC<FriendsListItemProps> = ({
  friendId,
  friendName,
  animonImgPath,
}) => {
  const [, setSessionToken] = useRecoilState(InvitationToken);
  const [, setInvitationId] = useRecoilState(InvitationSessionId);

  const IMGURL = `${animonImgPath}`;
  console.log(IMGURL);
  const navigate = useNavigate();

  const childId = useRecoilValue(Profilekey);

  const handleInvite = () => {
    const invitationSessionData = { childId, friendId };
    invite({
      invitationSessionData,
      onSuccess: data => {
        const { sessionId, token } = data;
        setInvitationId(sessionId);
        setSessionToken(token);
        console.log('초대에 보내는데 성공했습니다.');
        navigate(`/session`, { state: { childId, invitation: true } });
      },
      onError: () => {
        inputAlert('상대방이 현재 온라인 상태가 아닙니다.');
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
