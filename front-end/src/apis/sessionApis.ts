import instance from 'apis/instance';

// interface ApiResponse {
//   onSuccess: () => void;
//   onError: () => void;
// }

interface AnimonParams {
  subscriberId: number;
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getAnimon = async ({ subscriberId, onSuccess, onError }: AnimonParams) => {
  try {
    const response = await instance.get(`/children/participant/${subscriberId}`);
    console.log('Success Get Animon');
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};
