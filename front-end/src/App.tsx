import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import './App.css';
import MobilePage from 'pages/MobilePage/MobilePage';
import MatchPage from 'pages/MatchPage/MatchPage';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import MainPage from './pages/MainPage/MainPage';
import LoginPage from './pages/LoginPage/LoginPage';
import ProfilePage from './pages/ProfilePage/ProfilePage';
import SessionPage from './pages/SessionPage/SessionPage';
import FriendsPage from './pages/Friendspage/FriendsPage';
import RecordPage from './pages/RecordPage/RecordPage';
import BackgroundMusic from './components/main/BackgroundMusic';
import FireBase from './components/alarm/Alarm';

const theme = createTheme({
  typography: {
    fontFamily: [
      'Noto Sans KR',
      'Noto Sans',
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
  },
});

function App() {
  return (
    <RecoilRoot>
      <ThemeProvider theme={theme}>
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
            <Route path={'/mobile'} element={<MobilePage />} />
            <Route path={'/match'} element={<MatchPage />} />
          </Routes>
        </Router>
      </ThemeProvider>
    </RecoilRoot>
  );
}

export default App;
