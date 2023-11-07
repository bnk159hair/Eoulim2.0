import axios from 'axios';
import { Session } from 'openvidu-browser';
import instance from './instance';

interface User {
  childId: string;
  name: string;
  gender: string;
  school: string;
  grade: number;
}

export const getUserInfo = async (userId: string) => {
  try {
    const response = await instance.get(`/children/${userId}`);
    console.log('유저 정보 가져오기 성공!');
    console.log(response);
    return response.data.data;
  } catch (error) {
    console.log('유저 정보 가져오기 실패ㅠ');
    console.log(error);
    throw error;
  }
};

export const getToken = async (userInfo: User) => {
  console.log('토큰 가져오기');
  console.log(userInfo);
  try {
    const response = await instance.post(`/meetings/random/start`, userInfo);
    console.log('토큰 가져오기 성공!');
    console.log(response);
    return response.data.data;
  } catch (error) {
    console.log('토큰 가져오기 실패ㅠ');
    console.log(error);
    throw error;
  }
};

export const getFriendSessionToken = async (childId: string, sessionId: string) => {
  console.log('초대 토큰 가져오기');

  try {
    const response = await axios.post(`/meetings/friend/start`, {
      childId,
      sessionId,
    });
    console.log('초대 토큰 가져오기 성공!');
    console.log(response);
    return response.data.token;
  } catch (error) {
    console.log('초대 토큰 가져오기 실패ㅠ');
    console.log(error);
    throw error;
  }
};

export const destroySession = async (
  session: Session,
  guideScript: number[],
  timeStamp: string[],
) => {
  console.log('세션 파괴!!!!!!');
  console.log(session.sessionId, guideScript, timeStamp);
  try {
    const response = await axios.post(`/meetings/random/stop`, {
      sessionId: session.sessionId,
      guideSeq: guideScript,
      timeline: timeStamp,
    });
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};

export const destroyFriendSession = async (sessionId: string) => {
  console.log('친구 세션 녹화 종료');
  try {
    const response = await axios.post(`/meetings/friend/stop`, { sessionId });
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};
