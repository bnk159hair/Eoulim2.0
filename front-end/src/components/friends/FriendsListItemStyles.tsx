import styled from 'styled-components';
import friendbox from '../../assets/box/friend.png';
import invite from '../../assets/ecc/invite.png';

export const FriendCard = styled.div`
  padding: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 300px; /* Adjust the width as needed */
  height: 300px; /* Adjust the height as needed */
  margin: 10px;
  background-size: 100% 100%;
  background-image: url(${friendbox});
`;

export const FriendImg = styled.div`
  width: 200px; /* Larger width for the image */
  height: 200px; /* Larger height for the image */
  background-size: cover;
  background-position: center;
  margin: 10px;
`;

export const FrinedInfo = styled.div`
  margin-top: 12px;
  text-align: center;
  font-weight: bold;
  color: white;
  font-family: 'HakgyoansimBunpilR';
  font-size: 20px;
  flex-direction: column;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const InviteButton = styled.div`
  background-size: 100% 100%;
  background-image: url(${invite});
  width: 120px;
  height: 30px;
  margin: 7px;
  cursor: pointer;
  &:hover {
    transform: scale(1.25);
  }
`;
