import React, { useEffect } from 'react';
import {
  ModalOverlay,
  ModalContent,
  Accept,
  Refuse,
  FlexContent,
  EndMessage,
  ProfileImg,
} from './EndModalStyles';
import JSConfetti from "js-confetti";

interface EndModalProps {
  message: string;
  onClose: () => void;
  isAnimon: string;
}

const NewMaskModal: React.FC<EndModalProps> = ({ onClose, message, isAnimon }) => {
  const jsConfetti = new JSConfetti();
  const handleClick = () => {
    jsConfetti.addConfetti({
      confettiColors: [
        "#ff0a54",
        "#ff477e",
        "#ff7096",
        "#ff85a1",
        "#fbb1bd",
        "#f9bec7",
        ],
      confettiRadius: 5,
      confettiNumber: 500,
      });
    };

    useEffect(()=>{
        handleClick();
    },[]);
    return (
    <ModalOverlay>
      <ModalContent>
        <EndMessage>{message}</EndMessage>
        <FlexContent>
            <ProfileImg style={{ backgroundImage: `url(${isAnimon})` }} onClick={onClose} />
        </FlexContent>
      </ModalContent>
    </ModalOverlay>
  );
};

export default NewMaskModal;
