import instance from 'apis/instance';

interface RecordParams {
  childId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

interface RecordInfoParams {
  recordId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getRecords = async ({ childId, onSuccess, onError }: RecordParams) => {
  try {
    const response = await instance.get(`/recordings/${childId}`);
    console.log('녹화 영상 리스트 가져오기 성공');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const getRecordInfo = async ({ recordId, onSuccess, onError }: RecordInfoParams) => {
  try {
    const response = await instance.get(`/recordings/list/${recordId}`);
    console.log('녹화 영상 가져오기 성공');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
