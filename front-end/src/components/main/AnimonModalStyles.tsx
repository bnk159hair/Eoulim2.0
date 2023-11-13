import styled from 'styled-components';
import box from '../../assets/box/woodbox.jpg';
import before from '../../assets/ecc/before.png';
import after from '../../assets/ecc/after.png';

export const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ModalContent = styled.div`
  width: 800px;
  height: 200px;
  // background-color: white;
  border: 2px solid;
  padding: 20px;
  z-index: 1001;
  display: flex;
  justify-content: space-evenly;
  border-radius: 25px;
  align-items: center;
  background-image: url(${box});
`;

export const Animon = styled.div<{ animonurl?: string }>`
  width: 150px;
  height: 150px;
  background-image: url(${props => props.animonurl});
  background-size: cover;
  background-color: #87cefa;
  border-radius: 25px;
  border: solid 2px;
  cursor: pointer;
  &:hover {
    transform: scale(1.25);
  }
`;

export const BeforeButton = styled.div`
  height: 50px;
  width: 50px;
  background-image: url(${before});
  background-size: cover;
  cursor: pointer;
  &:hover {
    transform: scale(1.25);
  }
`;

export const AfterButton = styled.div`
  height: 50px;
  width: 50px;
  background-image: url(${after});
  background-size: cover;
  cursor: pointer;
  &:hover {
    transform: scale(1.25);
  }
`;
