import instance from 'apis/instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface FollowingData {
  childId: number;
  followingChildId: number;
}

interface VideoData {
  id: number;
  status: boolean;
}

interface AnimonParams {
  friendId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface ChangeVideoParams {
  videoData: VideoData;
  onSuccess: (isAnimonOn: boolean, message: string) => void;
  onError: () => void;
}

interface FollowingParams extends ApiResponse {
  followingData: FollowingData;
}

interface FriendsParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface ReceiveAnimonData {
  childId: number;
  otherChildId: number;
}

interface ReceiveAnimonParams {
  receiveAnimonData: ReceiveAnimonData;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const changeVideo = async ({ videoData, onSuccess, onError }: ChangeVideoParams) => {
  try {
    const isAnimonOn = !videoData.status;
    const jsonMessage = {
      childId: videoData.id,
      isAnimonOn,
    };
    const message = JSON.stringify(jsonMessage);
    onSuccess(isAnimonOn, message);
  } catch (error) {
    onError();
  }
};

export const follow = async ({ followingData, onSuccess, onError }: FollowingParams) => {
  try {
    console.log(followingData);
    await instance.post(`/follows`, followingData);
    onSuccess();
  } catch (error) {
    console.log(error);
    onError();
  }
};

// 친구 추가를 하면 상대방의 프로필 애니몬을 선물 받음.
export const receiveAnimon = async ({
  receiveAnimonData,
  onSuccess,
  onError,
}: ReceiveAnimonParams) => {
  try {
    // 얘는 V2임!
    // const tmp = API_V2_BASE_URL;
    // console.log(tmp);
    // instance.defaults.baseURL = API_V2_BASE_URL;
    // instance.defaults.baseURL = `http://localhost:8081/api/v2`;
    console.log('여기 왔따 !! TRY');
    const response = await instance.post(`/animons`, receiveAnimonData);
    onSuccess(response.data.data);
  } catch (error) {
    console.log('여기 왔따 !! ERROR');
    console.log(error);
    onError();
  } finally {
    console.log('여기 왔따 !! FINALLY');
    // instance.defaults.baseURL = API_BASE_URL;
  }
};

export const getAnimon = async ({ friendId, onSuccess, onError }: AnimonParams) => {
  try {
    const response = await instance.get(`/children/participant/${friendId}`);
    console.log('Success Get Animon');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const getFriends = async ({ childId, onSuccess, onError }: FriendsParams) => {
  try {
    const response = await instance.get(`/children/${childId}/follows`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
