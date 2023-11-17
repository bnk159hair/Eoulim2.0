import { atom } from 'recoil';

export const SessionId = atom<string>({
  key: 'sessionId',
  default: '',
});

export const PublisherId = atom<number>({
  key: 'publisherId',
  default: 0,
});

export const PublisherAnimonURL = atom<string>({
  key: 'publisherAnimonURL',
  default: '',
});

export const SubscriberId = atom<number>({
  key: 'subscriberId',
  default: 0,
});

export const SubscriberAnimonURL = atom<string>({
  key: 'subscriberAnimonURL',
  default: '',
});

export const IsAnimonLoaded = atom<boolean>({
  key: 'isAnimonLoaded',
  default: false,
});

export const guideSeq = atom<number[]>({
  key: 'guideSeq',
  default: [],
});

export const GuideScript = atom<number[]>({
  key: 'guideScript',
  default: [],
});

export const Timeline = atom<string[]>({
  key: 'timeline',
  default: [],
});
