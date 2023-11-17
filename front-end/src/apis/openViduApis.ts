import instance from './instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface UserData {
  childId: number;
  gender: string;
  grade: number;
  name: string;
  school: string;
}

interface InvitationSessionData {
  childId: number;
  friendId: number;
  sessionId?: string;
}
interface SessionData {
  sessionId: string;
  guideScript: number[];
  timeline: string[];
}

interface GetUserParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface GetTokenParams {
  userData: UserData;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface InvitationSessionParams {
  invitationSessionData: InvitationSessionData;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface SessionParams extends ApiResponse {
  sessionData: SessionData;
}

interface DestryInvitationSessionParams extends ApiResponse {
  sessionId: string;
}

export const getUserInfo = async ({ childId, onSuccess, onError }: GetUserParams) => {
  try {
    const response = await instance.get(`/children/${childId}`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const getToken = async ({ userData, onSuccess, onError }: GetTokenParams) => {
  try {
    const response = await instance.post(`/meetings/random/start`, userData);
    onSuccess(response.data.data);
  } catch (error) {
    console.log(error);
    onError();
  }
};

export const invite = async ({
  invitationSessionData,
  onSuccess,
  onError,
}: InvitationSessionParams) => {
  try {
    const response = await instance.post(`/meetings/friend/start`, invitationSessionData);
    onSuccess(response.data.data);
  } catch (error: any) {
    if (error.response.data.status === '404 NOT_FOUND') {
      onError();
    } else {
      console.log('초대에 실패했습니다.', error);
    }
  }
};

export const destroySession = async ({ sessionData, onSuccess, onError }: SessionParams) => {
  try {
    await instance.post(`/meetings/random/stop`, sessionData);
    onSuccess();
  } catch (error) {
    console.log(error);
    onError();
  }
};

export const destroyInvitationSession = async ({
  sessionId,
  onSuccess,
  onError,
}: DestryInvitationSessionParams) => {
  try {
    await instance.post(`/meetings/friend/stop`, { sessionId });
    onSuccess();
  } catch (error) {
    console.log(error);
    onError();
  }
};
