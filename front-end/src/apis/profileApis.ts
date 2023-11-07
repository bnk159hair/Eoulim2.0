import instance from './instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface ChildData {
  name: string;
  birth: string;
  gender: string;
  school: string;
  grade: string;
}

interface ChildLoginoutData {
  childId: number;
  fcmToken: string;
}

interface CreateProfileParams extends ApiResponse {
  childData: ChildData;
}

interface ChildLoginParams extends ApiResponse {
  childLoginData: ChildLoginoutData;
}

interface ChildLogoutParams extends ApiResponse {
  childLogoutData: ChildLoginoutData;
}

interface CheckSchoolParams {
  school: string;
  onSuccess: (data: boolean) => void;
  onError: () => void;
}

interface GetChildInfoParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const postCreateProfile = async ({ childData, onSuccess, onError }: CreateProfileParams) => {
  try {
    await instance.post(`/children`, childData);
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const postCheckSchool = async ({ school, onSuccess, onError }: CheckSchoolParams) => {
  try {
    const response = await instance.post(`/open-api/schools`, {
      keyword: school,
    });
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const childLogin = async ({ childLoginData, onSuccess, onError }: ChildLoginParams) => {
  try {
    await instance.post('/children/login', {
      childId: childLoginData.childId,
      fcmToken: childLoginData.fcmToken,
    });
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const childLogout = async ({ childLogoutData, onSuccess, onError }: ChildLogoutParams) => {
  try {
    await instance.post('/children/logout', {
      childId: childLogoutData.childId,
      fcmToken: childLogoutData.fcmToken,
    });
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const getChildInfo = async ({ childId, onSuccess, onError }: GetChildInfoParams) => {
  try {
    const response = await instance.get(`/children/${childId}`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
