import React, { useRef } from 'react';
import { StreamManager } from 'openvidu-browser';
import { useFaceMask } from '../../hooks/useFaceMesh';
import { useStream } from '../../hooks/useStream';
import { Canvas, UserName, Video } from './StreamCanvasStyles';

interface IProps {
  streamManager: StreamManager;
  name: string;
  avatarPath: string;
  videoState: boolean;
}

export const StreamCanvas = ({ streamManager, name, avatarPath, videoState }: IProps) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const { videoRef } = useStream(streamManager);
  console.log(
    '여기 videoRef:  !!!!',
    videoRef.current?.clientWidth,
    videoRef.current?.clientHeight,
  );
  useFaceMask(videoRef.current, canvasRef.current, avatarPath);

  return (
    <>
      {console.log(
        '여기 canvasRef:  !!!!',
        canvasRef.current?.clientWidth,
        canvasRef.current?.clientHeight,
      )}
      <Canvas ref={canvasRef} videoState={videoState} />
      <Video ref={videoRef} videoState={videoState} />
      <UserName>{name}</UserName>
    </>
  );
};

export default StreamCanvas;
