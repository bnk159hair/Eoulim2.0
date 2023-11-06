import instance from 'apis/instance';

interface RecordParams {
  profileId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getRecord = async ({ profileId, onSuccess, onError }: RecordParams) => {
  try {
    const response = await instance.get(`/recordings/${profileId}`);
    console.log('녹화 영상 가져오기 성공');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
