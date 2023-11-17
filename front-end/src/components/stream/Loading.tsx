import React, { useState, useEffect } from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import { SpinnerContainer, AnimalEmoji, Spinner } from './LoadingStyles';

interface IProps {
  isAnimonLoaded: boolean;
}

const Loading = ({ isAnimonLoaded }: IProps) => {
  const [screenWidth, setScreenWidth] = useState(window.innerWidth);
  const [animal, setAnimal] = useState('🐱');
  useEffect(() => {
    const animalArray = [
      '🐱',
      '🐶',
      '🐰',
      '🦊',
      '🐷',
      '🐹',
      '🦁',
      '🐸',
      '🐯',
      '🦄',
      '🐻',
      '🐵',
      '🐲',
    ];
    const randomAnimal = setInterval(() => {
      const animalIndex = Math.floor(Math.random() * animalArray.length);

      const newAnimal = animalArray[animalIndex];

      setAnimal(newAnimal);
    }, 1400);

    return () => {
      clearInterval(randomAnimal);
    };
  }, []);

  useEffect(() => {
    const handleResize = () => {
      setScreenWidth(window.innerWidth);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);
  
  const size = screenWidth <= 481 ? 100 : 200;

  return (
    <SpinnerContainer isAnimonLoaded={isAnimonLoaded}>
      <Spinner>
        <CircularProgress size={size} />
        <AnimalEmoji>{animal}</AnimalEmoji>
      </Spinner>
    </SpinnerContainer>
  );
};

export default Loading;
