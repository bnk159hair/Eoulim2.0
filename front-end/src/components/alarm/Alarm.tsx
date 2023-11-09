import React, { useState } from 'react';
import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, isSupported, onMessage } from 'firebase/messaging';
import { fcmTokenState } from 'atoms/Firebase';
import { useRecoilState } from 'recoil';
import AlarmModal from 'components/main/AlarmModal';

export const FireBase = () => {
  const [fcmToken, setFcmToken] = useRecoilState(fcmTokenState);
  const [isAlarmOpen, setAlarmOpen] = useState(false);
  const [sessionId, setSessionId] = useState('');
  const [userName, setUserName] = useState('');

  isSupported().then(response => {
    if (response) {
      return null;
    }
  });

  const config = {
    // 프로젝트 설정 > 일반 > 하단의 내앱에서 확인
    apiKey: 'AIzaSyBE6RMlHyq3QKhYxdsZITddOkvfQNMcETI',
    authDomain: 'eoullim-7e5fb.firebaseapp.com',
    databaseURL: 'https://eoullim-7e5fb-default-rtdb.asia-southeast1.firebasedatabase.app',
    projectId: 'eoullim-7e5fb',
    storageBucket: 'eoullim-7e5fb.appspot.com',
    messagingSenderId: '54110600029',
    appId: '1:54110600029:web:479d213e11420c0b5946b4',
    measurementId: 'G-2957PMMRBN',
  };

  const app = initializeApp(config);
  const messaging = getMessaging(app);

  if (fcmToken === '') {
    getToken(messaging, {
      vapidKey:
        'BBOa5aFnA-ro3kuLdxF-k7Ifro9nF11wfu7w7Eh97ArUgUsO1vEQkHOznBwOlJe4wnNha6mHHcyRZV08qNtOb_Y',
    })
      .then(currentToken => {
        if (currentToken) {
          // Send the token to your server and update the UI if necessary
          // ...
          setFcmToken(currentToken);
          console.log(currentToken);
        } else {
          // Show permission request UI
          console.log('No registration token available. Request permission to generate one.');
          // ...
        }
      })
      .catch(err => {
        console.log('An error occurred while retrieving token. ', err);
        // ...
      });
  } else {
    console.log(fcmToken);
  }
  // 포그라운드 메시지 수신
  onMessage(messaging, payload => {
    console.log('Message received. ', payload);
    if (payload.notification) {
      if (payload.notification.title) {
        setUserName(payload.notification.title.split(' ')[0]);
      }
      if (payload.notification.body) {
        setSessionId(payload.notification.body.split(' ')[-1]);
      }
      setAlarmOpen(true);
    }
    // ...
  });

  const closeAlarm = () => {
    setAlarmOpen(false);
  };

  return (
    <div>
      {isAlarmOpen && <AlarmModal onClose={closeAlarm} sessionId={sessionId} userName={userName} />}
    </div>
  );
};

export default FireBase;
