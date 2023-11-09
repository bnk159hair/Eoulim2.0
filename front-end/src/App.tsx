import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
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
