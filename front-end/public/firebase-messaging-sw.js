// 프로젝트 버전 확인
importScripts('https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/9.5.0/firebase-messaging.js');

const config = {
  apiKey: 'AIzaSyBE6RMlHyq3QKhYxdsZITddOkvfQNMcETI',
  authDomain: 'eoullim-7e5fb.firebaseapp.com',
  databaseURL: 'https://eoullim-7e5fb-default-rtdb.asia-southeast1.firebasedatabase.app',
  projectId: 'eoullim-7e5fb',
  storageBucket: 'eoullim-7e5fb.appspot.com',
  messagingSenderId: '54110600029',
  appId: '1:54110600029:web:479d213e11420c0b5946b4',
  measurementId: 'G-2957PMMRBN',
};

// Initialize Firebase
firebase.initializeApp(config);

const messaging = firebase.messaging();

// 백그라운드 서비스워커 설정
messaging.onBackgroundMessage(messaging, payload => {
  console.log('[firebase-messaging-sw.js] Received background message ', payload);

  // Customize notification here
  const notificationTitle = 'Background Message Title';
  const notificationOptions = {
    body: payload,
    icon: '/firebase-logo.png',
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});
