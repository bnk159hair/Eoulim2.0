import { atom } from 'recoil';

export const InvitationToken = atom<string>({
  key: 'invitationToken',
  default: '',
});

export const InvitationSessionId = atom<string>({
  key: 'invitationSessionId',
  default: '',
});
