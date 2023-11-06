import instance from './instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface ProfileData {
  name: string;
  birth: string;
  gender: string;
  school: string;
  grade: string;
}

interface CreateProfileParams extends ApiResponse {
  profileData: ProfileData;
}

interface CheckSchoolParams {
  school: string;
  onSuccess: (data: boolean) => void;
  onError: () => void;
}

export const postCreateProfile = async ({
  profileData,
  onSuccess,
  onError,
}: CreateProfileParams) => {
  try {
    await instance.post(`/children`, profileData);
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
