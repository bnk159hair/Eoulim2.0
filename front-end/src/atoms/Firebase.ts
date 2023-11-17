import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist();

export const fcmTokenState = atom<string>({
  key: 'fcmTokenState',
  default: '',
  effects_UNSTABLE: [persistAtom],
});
