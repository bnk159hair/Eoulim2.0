import instance from './instance';

interface ApiResponse {
  onSuccess: (data: any) => void;
  onError: () => void;
}

export const getNotifications = async ({ onSuccess, onError }: ApiResponse) => {
  try {
    const response = await instance.get('/notifications');
    console.log(response);
    onSuccess(response.data.data);
  } catch (error) {
    console.log(error);
    onError();
  }
};

export const deleteNotifications = async () => {
  try {
    const response = await instance.delete('/notifications');
    console.log(response);
  } catch (error) {
    console.log(error);
  }
};
