import styled, { keyframes } from 'styled-components';
import loginBackground from '../../assets/background/login.gif';
import password from '../../assets/ecc/password.png';
import bell from '../../assets/ecc/bell.png';

export const ProfilePageContainer = styled.div`
  height: 100vh;
  background-size: 100% 100%;
  background-image: url(${loginBackground});
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

const bellShakeAnimation = keyframes`
  10%, 90% {
    transform: rotate(-5deg);
  }
  20%, 80% {
    transform: rotate(5deg);
  }
  30%, 50%, 70% {
    transform: rotate(-5deg);
  }
  40%, 60% {
    transform: rotate(5deg);
  }
  100% {
    transform: rotate(0deg);
  }
`;

export const Alarm = styled.div`
  position: relative;
  background-size: 100% 100%;
  background-image: url(${bell});
  width: 80px;
  height: 80px;
  cursor: pointer;
  &:hover {
    animation: ${bellShakeAnimation} 0.5s ease;
  }
`;

export const NotificationBadge = styled.div<{ $isReadNotifications: boolean }>`
  position: absolute;
  top: -5px;
  right: 20px;
  width: 30px;
  height: 30px;
  background-color: tomato;
  border-radius: 50%;
  display: ${props => (props.$isReadNotifications ? 'flex' : 'none')};
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 20px;
`;

export const PasswordChange = styled.div`
  background-size: 100% 100%;
  background-image: url(${password});
  width: 80px;
  height: 80px;
  margin-left: auto;
  cursor: pointer;
  &:hover {
    transform: scale(1.1);
  }
`;

export const MarginContainer = styled.div`
  margin: 20px;
  display: flex;
  justify-content: flex-end;
  button {
    font-size: 24px;
    background-color: red;
    color: white;
    border-radius: 10px;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  button:hover {
    background-color: #c90000;
  }
`;
