import instance from './instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface ChangeAnimonParams extends ApiResponse {
  animonId: number;
  childId: number;
}

interface GetAnimonParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getAnimons = async ({ childId, onSuccess, onError }: GetAnimonParams) => {
  try {
    const response = await instance.get(`/children/${childId}/animons`);
    onSuccess(response.data.data);
  } catch (error) {
    console.log(error);
    onError();
  }
};

export const changeAnimon = async ({
  animonId,
  childId,
  onSuccess,
  onError,
}: ChangeAnimonParams) => {
  try {
    await instance.patch(`/children/${childId}/animons`, { animonId });
    onSuccess();
  } catch (error) {
    onError();
  }
};
