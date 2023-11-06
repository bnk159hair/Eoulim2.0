import axios, { AxiosInstance } from 'axios';
import { API_BASE_URL } from './urls';
import instance from './instance';

interface ApiResponse {
  onSuccess: () => void;
  onError: () => void;
}

interface LoginData {
  username: string;
  password: string;
}

interface SignupData {
  username: string;
  password: string;
  name: string;
  phoneNumber: string;
}

interface PasswordData {
  curPassword: string;
  newPassword: string;
}

interface LoginParams extends ApiResponse {
  loginData: LoginData;
}

interface SignupParams extends ApiResponse {
  signupData: SignupData;
}

interface CheckUsernameParams {
  username: string;
  onSuccess: (data: boolean) => void;
  onError: () => void;
}

interface ChangePasswordParams extends ApiResponse {
  passwordData: PasswordData;
}

const userInstance: AxiosInstance = axios.create({
  baseURL: `${API_BASE_URL}/users`,
  headers: {
    'Content-Type': 'application/json',
  },
});

const TOKEN = 'accessToken';
const isAccessTokenEmpty = localStorage.getItem(TOKEN) === null;

const saveToken = (token: string) => {
  if (isAccessTokenEmpty) {
    localStorage.setItem(TOKEN, token);
  } else {
    localStorage.removeItem(TOKEN);
    localStorage.setItem(TOKEN, token);
  }
};

const removeToken = () => {
  if (!isAccessTokenEmpty) {
    localStorage.removeItem(TOKEN);
  }
};

export const postLogin = async ({ loginData, onSuccess, onError }: LoginParams) => {
  try {
    const response = await userInstance.post('/login', loginData);
    saveToken(response.data.data);
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const postSignup = async ({ signupData, onSuccess, onError }: SignupParams) => {
  try {
    await userInstance.post('/join', signupData);
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const getCheckUsername = async ({ username, onSuccess, onError }: CheckUsernameParams) => {
  try {
    const response = await userInstance.get(`check-username/${username}`);
    onSuccess(response.data.data);
  } catch (error) {
    onError();
  }
};

export const getLogout = async ({ onSuccess, onError }: ApiResponse) => {
  try {
    await instance.get('/users/logout');
    removeToken();
    onSuccess();
  } catch (error) {
    onError();
  }
};

export const patchChangePassword = async ({
  passwordData,
  onSuccess,
  onError,
}: ChangePasswordParams) => {
  try {
    await instance.patch('/users/password', passwordData);
    onSuccess();
  } catch (error) {
    onError();
  }
};
