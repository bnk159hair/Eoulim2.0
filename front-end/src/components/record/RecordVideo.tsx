import React, { useEffect, useRef, useState } from 'react';
import CloseIcon from '@mui/icons-material/Close';
import { IconButton, Button } from '@mui/material';
import ReactPlayer from 'react-player';
import { getRecordInfo } from 'apis/recordApis';
import {
  ModalOverlay,
  ModalContent,
  HeaderContainer,
  FormContainer,
  VideoInfo,
  GuideInfo,
  GuideContainer,
} from './RecordVideoStyles';

interface VideoModalProps {
  onClose: () => void;
  videoPath: string;
  recordId: number;
}

interface Guide {
  content: string;
  timeline: string;
}

interface RecordInfo {
  animonName: string;
  createTime: string;
  guideInfo: Guide[];
  id: number;
  name: string;
  school: string;
  videoPath: string;
}

interface GuideInfo {
  content: string;
  time: string;
  second: number;
}

const VideoModal: React.FC<VideoModalProps> = ({ onClose, videoPath, recordId }) => {
  const [recordInfo, setRecordInfo] = useState<RecordInfo | null>(null);

  useEffect(() => {
    getRecordInfo({
      recordId,
      onSuccess: data => {
        console.log(data);
        setRecordInfo(data);
      },
      onError: () => {
        console.log('녹화 정보를 불러오는데 실패하였습니다.');
      },
    });
  }, []);

  const videoRef = useRef<any>(null);
  const controlsRef = useRef<any>(null);

  const [state, setState] = useState({
    playing: true, // 재생중인지
    muted: false, // 음소거인지
    controls: false, // 기본으로 제공되는 컨트롤러 사용할건지
    volume: 0.5, // 볼륨크기
    playbackRate: 1.0, // 배속
    played: 0, // 재생의 정도 (value)
    seeking: false, // 재생바를 움직이고 있는지
    duration: 0, // 전체 시간
  });

  const { playing, muted, volume, playbackRate } = state;

  let count = 0;

  const format = (seconds: string) => {
    if (Number.isNaN(Number(seconds))) {
      return `00:00`;
    }
    const date = new Date(Number(seconds));
    const hh = date.getUTCHours();
    const mm = date.getUTCMinutes();
    const ss = pad(date.getUTCSeconds());
    if (hh) {
      return `${hh}:${pad(mm)}:${ss}`;
    }
    return `${mm}:${ss}`;
  };

  function pad(string: number) {
    return `0${String(string)}`.slice(-2);
  }

  const info: GuideInfo[] = [];

  if (recordInfo && recordInfo.guideInfo.length) {
    recordInfo.guideInfo.forEach(({ content, timeline }) => {
      const time = format(timeline);
      if (content && timeline) {
        info.push({ content, time, second: Number(timeline) / 1000 });
      }
    });
    console.log(info);
  }

  const progressHandler = (changeState: any) => {
    if (controlsRef.current && count > 3) {
      controlsRef.current.style.visibility = 'hidden';
      count = 0;
    }
    if (controlsRef.current && controlsRef.current.style.visibility === 'visible') {
      count += 1;
    }
    if (!state.seeking) {
      setState({ ...state, ...changeState });
    }
  };

  return (
    <ModalOverlay>
      <ModalContent>
        <HeaderContainer>
          <IconButton onClick={onClose}>
            <CloseIcon fontSize={'large'} />
          </IconButton>
        </HeaderContainer>
        <FormContainer>
          <ReactPlayer
            ref={videoRef}
            url={videoPath} // 서버에서 받아온 video url
            playing={playing} // true = 재생중 / false = 멈춤
            controls // 기본 컨트롤러 사용
            muted={muted} // 음소거인지
            volume={volume} // 소리조절 기능
            playbackRate={playbackRate} // 배속기능
            onProgress={progressHandler} // 재생 및 로드된 시점을 반환
          />
          <VideoInfo>
            <GuideContainer>
              {info.length ? (
                info.map(({ content, second, time }) => (
                  <div className={'timestamp_box'} key={second}>
                    <Button
                      variant={'contained'}
                      color={'primary'}
                      size={'small'}
                      style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                      }}
                      onClick={() => {
                        videoRef.current.seekTo(second);
                      }}
                    >
                      <span>{time}</span>
                    </Button>
                    <GuideInfo>{content}</GuideInfo>
                  </div>
                ))
              ) : (
                <GuideInfo>{'타임라인이 없습니다'}</GuideInfo>
              )}
            </GuideContainer>
          </VideoInfo>
        </FormContainer>
      </ModalContent>
    </ModalOverlay>
  );
};

export default VideoModal;
