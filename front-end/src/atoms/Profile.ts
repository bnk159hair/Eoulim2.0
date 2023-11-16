import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

interface Animon {
  id: number;
  bodyImagePath: string;
  maskImagePath: string;
  name: string;
}

interface ChildProfile {
  profileAnimon: Animon;
  id: number;
  name: string;
  birth: string;
  gender: string;
  school: string;
  grade: string;
  status: string;
}

const { persistAtom } = recoilPersist();

export const Profilekey = atom<number>({
  key: 'profileId',
  default: 0,
  effects_UNSTABLE: [persistAtom],
});

export const Profile = atom<ChildProfile>({
  key: 'profile',
  default: {
    id: 0,
    name: '짱구아들',
    birth: '1399248000000',
    gender: 'M',
    school: '떡잎초등학교',
    grade: '3',
    status: 'ON',
    profileAnimon: {
      id: 4,
      bodyImagePath: 'tiger_head',
      maskImagePath: 'tiger_body',
      name: 'tiger',
    },
  },
});
