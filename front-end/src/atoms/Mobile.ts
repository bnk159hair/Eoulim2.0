import { atom } from 'recoil';

export const MobileChildId = atom<number>({
  key: 'mobileChildId',
  default: 0,
});
