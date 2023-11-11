import React, { useEffect } from 'react';
import { MobileChildId } from 'atoms/Mobile';
import { useNavigate } from 'react-router';
import { useRecoilState } from 'recoil';
import { getChildInfo } from 'apis/profileApis';
import { Profile } from 'atoms/Profile';

export const Mobile = () => {
  const navigate = useNavigate();
  const [childId, setChildId] = useRecoilState(MobileChildId);
  const [, setProfile] = useRecoilState(Profile);

  const TOKEN = 'accessToken';
  const isAccessTokenEmpty = localStorage.getItem(TOKEN) === null;

  const saveToken = (token: string) => {
    if (isAccessTokenEmpty) {
      localStorage.setItem(TOKEN, token);
    } else {
      localStorage.removeItem(TOKEN);
      localStorage.setItem(TOKEN, token);
    }
  };

  useEffect(() => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    window.changePage = message => {
      const data = JSON.parse(message);
      console.log(`invitation: ${data.invitation}, childId: ${data.childId}`);
      if (data) {
        const { invitation, childId, token } = JSON.parse(message);
        console.log(`invitation: ${invitation}, childId: ${childId}, token: ${token}`);
        if (token) {
          saveToken(token);
        }

        getChildInfo({
          id: childId,
          onSuccess: data => {
            setProfile(data);
            console.log('프로필 가져오기에 성공하였습니다.');
            console.log(data);
          },
          onError: () => {
            console.log('프로필 가져오기에 실패하였습니다.');
          },
        });

        if (childId && token && invitation === false) {
          console.log('모바일에서 새 친구 세션으로 이동합니다.');
          setChildId(data.childId);
          navigate('/session', { state: { childId, invitation: false } });
        } else if (childId && token && invitation === true) {
          console.log('모바일에서 내 친구 세션으로 이동합니다.');
          setChildId(data.childId);
          navigate('/session', { state: { childId, invitation } });
        } else {
          console.log('모바일에서 웹 세션으로 접속에 실패하였습니다.');
          console.log(data);
          console.log(typeof data);
          console.log(invitation, childId, token);
        }
      }
    };
  }, [childId]);

  return <div style={{ visibility: 'hidden' }} />;
};

export default Mobile;
