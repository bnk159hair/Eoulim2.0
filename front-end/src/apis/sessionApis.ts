import instance from 'apis/instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface FollowingData {
  myId: number;
  friendId: number;
}

interface VideoData {
  id: number;
  status: boolean;
}

interface AnimonParams {
  subscriberId: number;
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
  profileId: number;
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
    await instance.post(`/friendship`, {
      followingData,
    });
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const getAnimon = async ({ subscriberId, onSuccess, onError }: AnimonParams) => {
  try {
    const response = await instance.get(`/children/participant/${subscriberId}`);
    console.log('Success Get Animon');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const getFriends = async ({ profileId, onSuccess, onError }: FriendsParams) => {
  try {
    const response = await instance.get(`/children/${profileId}/follows`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
