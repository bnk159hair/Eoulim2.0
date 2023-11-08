import instance from './instance';

interface getFollowingsParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getFollowings = async ({ childId, onSuccess, onError }: getFollowingsParams) => {
  try {
    const response = await instance.get(`/children/${childId}/follows`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
