import React, { useState, useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { getFollowings } from 'apis/followingApis';
import FriendsListItem from './FriendsListItem';
import { Profilekey } from '../../atoms/Profile';
import { EmptyFriend, BeforeButton, AfterButton, FriendsListContent } from './FriendsListStyles';

interface FriendsProfile {
  gender: string;
  grade: number;
  id: number;
  name: string;
  profileAnimon: { bodyImagePath: string; id: number; maskImagePath: string; name: string };
  school: string;
}

const FriendsList = () => {
  const childId = useRecoilValue(Profilekey);
  const [friends, setFriends] = useState<FriendsProfile[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const friendsPerPage = 3; // 페이지당 보여줄 친구 수 설정

  useEffect(() => {
    getFollowings({
      childId,
      onSuccess: data => {
        setFriends(data);
        console.log(data);
      },
      onError: () => {
        console.log('친구목록 불러오기 오류');
      },
    });
  }, [childId]);

  const indexOfLastFriend = currentPage * friendsPerPage;
  const indexOfFirstFriend = indexOfLastFriend - friendsPerPage;
  const currentFriends = friends.slice(indexOfFirstFriend, indexOfLastFriend);

  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };

  return (
    <div style={{ display: 'flex', alignItems: 'center' }}>
      {friends.length > friendsPerPage && (
        <div style={{ width: '80px' }}>
          {currentPage > 1 && <BeforeButton onClick={prevPage} />}
        </div>
      )}
      <FriendsListContent>
        {/* <div
        style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', width: '1200px' }}
      > */}
        {currentFriends.length > 0 ? (
          currentFriends.map(friend => (
            <FriendsListItem
              key={friend.id}
              friendId={friend.id}
              friendName={friend.name}
              animonImgPath={friend.profileAnimon.bodyImagePath}
            />
          ))
        ) : (
          <EmptyFriend />
        )}
        {/* </div> */}
      </FriendsListContent>
      {friends.length > friendsPerPage && (
        <div style={{ width: '80px' }}>
          {currentPage < Math.ceil(friends.length / friendsPerPage) && (
            <AfterButton onClick={nextPage} />
          )}
        </div>
      )}
    </div>
  );
};

export default FriendsList;
