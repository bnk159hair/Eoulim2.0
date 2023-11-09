import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import './App.css';
import MainPage from './pages/MainPage/MainPage';
import LoginPage from './pages/LoginPage/LoginPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import SessionPage from './pages/SessionPage/SessionPage';
import FriendsPage from './pages/Friendspage/FriendsPage';
import RecordPage from './pages/RecordPage/RecordPage';
import BackgroundMusic from './components/main/BackgroundMusic';
import FireBase from './components/alarm/Alarm';

function App() {
  const navigate = useNavigate();

  useEffect(() => {
    // @ts-ignore
    window.changePage = (message: Message) => {
      switch (message.invitation) {
        case false:
          navigate('/session', { state: { childId: message.childId, invitation: false } });
          break;
        case true:
          navigate('/session', { state: { childId: message.childId, invitation: true } });
          break;
        default:
          console.log('값이 존재하지 않습니다.');
          break;
      }
    };
  });

  return (
    <RecoilRoot>
      <Router>
        <BackgroundMusic />
        <FireBase />
        <Routes>
          <Route path={'/'} element={<MainPage />} />
          <Route path={'/login'} element={<LoginPage />} />
          <Route path={'/profile'} element={<ProfilePage />} />
          <Route path={'/session'} element={<SessionPage />} />
          <Route path={'/friends'} element={<FriendsPage />} />
          <Route path={'/record'} element={<RecordPage />} />
        </Routes>
      </Router>
    </RecoilRoot>
  );
}

export default App;
