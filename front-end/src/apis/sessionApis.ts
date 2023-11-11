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
