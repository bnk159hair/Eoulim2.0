import React, { useState, useEffect } from 'react';
import {
  Button,
  IconButton,
  InputAdornment,
  TextField,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import CloseIcon from '@mui/icons-material/Close';
import dayjs from 'dayjs';
import Swal from 'sweetalert2';
import { postCheckPassword } from 'apis/authApis';
import inputAlert from 'utils/inputAlert';
import { deleteChild, getChildInfo, postCheckSchool, putModifyChild } from 'apis/profileApis';
import {
  ModalOverlay,
  ModalContent,
  FormContainer,
  ButtonContainer,
  HeaderContainer,
  FlexContainer,
} from './ModifyModalStyles';

interface ChildData {
  id: number;
  name: string;
  birth: string;
  gender: string;
  school: string;
  grade: string;
  status: string;
}

interface Props {
  id: number;
  onClose: () => void;
  getChildren: () => void;
}

const ModifyModal = ({ id, onClose, getChildren }: Props) => {
  const [childData, setChildData] = useState<ChildData>({
    id,
    name: '',
    birth: '',
    gender: '',
    school: '',
    grade: '',
    status: '',
  });

  const [password, setPassword] = useState('');
  const [isPasswordCorrect, setIsPasswordCorrect] = useState(false);
  const [, setSchoolChange] = useState(false);
  const [isSchoolCorrect, setIsSchoolCorrect] = useState(false);
  const namePattern = /^[가-힣]{2,4}$/;
  const isValidName = namePattern.test(childData.name);

  useEffect(() => {
    getChildInfo({
      id,
      onSuccess: data => {
        setChildData(data);
      },
      onError: () => {
        inputAlert('아이 프로필을 불러오는데 실패했습니다.');
      },
    });
  }, [id]);

  const handlePasswordCheck = (
    event: React.MouseEvent<HTMLButtonElement> | React.FormEvent<HTMLFormElement>,
  ) => {
    event.preventDefault();
    postCheckPassword({
      password,
      onSuccess: isCorrect => {
        if (isCorrect) {
          inputAlert('비밀번호가 확인되었습니다!', false).then(() => setIsPasswordCorrect(true));
        } else {
          inputAlert('비밀번호를 확인해주세요!');
        }
      },
      onError: () => {
        inputAlert('잠시 후 다시 시도해주세요!');
      },
    });
  };

  const handleModifyChild = () => {
    if (
      !childData.name.trim() ||
      !childData.birth ||
      !childData.gender ||
      !childData.school ||
      !childData.grade
    ) {
      inputAlert('모든 정보를 입력해주세요!');
      return;
    }

    if (!isSchoolCorrect) {
      inputAlert('학교 확인을 해주세요!');
      return;
    }

    if (!isValidName) {
      inputAlert('이름을 다시 입력해주세요!');
      return;
    }

    putModifyChild({
      childData,
      onSuccess: () => {
        inputAlert('수정 완료했습니다!', false).then(() => {
          getChildren();
          onClose();
        });
      },
      onError: () => {
        inputAlert('프로필 수정에 실패했습니다!');
      },
    });
  };

  const handleSchoolCheck = () => {
    postCheckSchool({
      school: childData.school,
      onSuccess: isCorrect => {
        setIsSchoolCorrect(isCorrect);
        if (isCorrect) {
          inputAlert('올바른 학교정보입니다!', false);
        } else {
          inputAlert('다시 입력해주세요!');
        }
      },
      onError: () => {
        inputAlert('잘못된 입력입니다!');
      },
    });
  };

  const handleDeleteChild = () => {
    Swal.fire({
      title: `${childData.name} 정보를 삭제하시겠습니까?`,
      text: '되돌릴 수 없습니다!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
    }).then(result => {
      if (result.isConfirmed) {
        deleteChild({
          id,
          onSuccess: () => {
            inputAlert('삭제되었습니다!', false).then(() => getChildren());
          },
          onError: () => {
            inputAlert('잠시 후 다시 시도해주세요!');
          },
        });
      }
    });
  };

  return (
    <ModalOverlay>
      <ModalContent>
        {!isPasswordCorrect ? (
          <FormContainer onSubmit={handlePasswordCheck}>
            <h2>{'비밀번호 확인'}</h2>
            <TextField
              label={'비밀번호 확인'}
              variant={'outlined'}
              margin={'dense'}
              type={'password'}
              value={password}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) =>
                setPassword(event.target.value)
              }
            />
            <ButtonContainer>
              <Button
                variant={'contained'}
                size={'small'}
                sx={{ fontSize: '18px', margin: '0.5rem' }}
                onClick={handlePasswordCheck}
                fullWidth
              >
                {'확인'}
              </Button>
              <Button
                variant={'contained'}
                size={'small'}
                sx={{ fontSize: '18px', margin: '0.5rem' }}
                onClick={onClose}
                fullWidth
              >
                {'닫기'}
              </Button>
            </ButtonContainer>
          </FormContainer>
        ) : (
          <FormContainer>
            <HeaderContainer>
              <h2>{'프로필 수정'}</h2>
              <IconButton onClick={onClose}>
                <CloseIcon fontSize={'large'} />
              </IconButton>
            </HeaderContainer>
            <FlexContainer>
              <TextField
                label={'이름'}
                variant={'outlined'}
                placeholder={'홍길동'}
                value={childData.name}
                onChange={event => {
                  setChildData(prev => ({
                    ...prev,
                    name: event.target.value,
                  }));
                }}
                sx={{ width: '65%', marginBottom: '1rem' }}
              />
              <ToggleButtonGroup
                color={'primary'}
                value={childData.gender}
                exclusive
                sx={{ marginLeft: 'auto' }}
                size={'large'}
                onChange={(_, newGender) => {
                  setChildData(prev => ({
                    ...prev,
                    gender: newGender,
                  }));
                }}
              >
                <ToggleButton value={'M'}>{'남성'}</ToggleButton>
                <ToggleButton value={'W'}>{'여성'}</ToggleButton>
              </ToggleButtonGroup>
            </FlexContainer>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label={'생년월일'}
                value={dayjs(childData.birth)}
                onChange={(newDate: dayjs.Dayjs | null) => {
                  if (newDate) {
                    setChildData((prev: any) => ({
                      ...prev,
                      birth: newDate.format('YYYY-MM-DD'),
                    }));
                  }
                }}
                format={'YYYY-MM-DD'}
                sx={{ marginBottom: '1rem' }}
              />
            </LocalizationProvider>
            <FlexContainer>
              <TextField
                label={'학교 이름'}
                variant={'outlined'}
                value={childData.school}
                onChange={event => {
                  setChildData(prev => ({
                    ...prev,
                    school: event.target.value,
                  }));
                  setSchoolChange(true);
                }}
                sx={{ width: '65%', marginBottom: '1rem' }}
                InputProps={{
                  endAdornment: <InputAdornment position={'end'}>{'초등학교'}</InputAdornment>,
                }}
                helperText={isSchoolCorrect && '학교 등록이 완료되었습니다.'}
                disabled={isSchoolCorrect && true}
              />
              <Button
                variant={'contained'}
                size={'large'}
                sx={{
                  width: '25%',
                  padding: '0.7rem',
                  marginLeft: 'auto',
                  fontSize: '18px',
                }}
                onClick={handleSchoolCheck}
              >
                {'학교확인'}
              </Button>
            </FlexContainer>
            <ToggleButtonGroup
              color={'primary'}
              value={childData.grade}
              exclusive
              fullWidth
              onChange={(_, newGrade) => {
                setChildData(prev => ({
                  ...prev,
                  grade: newGrade,
                }));
              }}
            >
              <ToggleButton value={'1'}>{'1학년'}</ToggleButton>
              <ToggleButton value={'2'}>{'2학년'}</ToggleButton>
              <ToggleButton value={'3'}>{'3학년'}</ToggleButton>
            </ToggleButtonGroup>
            <ButtonContainer>
              <Button
                variant={'contained'}
                size={'small'}
                sx={{
                  width: '47%',
                  padding: '0.6rem',
                  marginTop: '1rem',
                  fontSize: '18px',
                }}
                onClick={handleModifyChild}
              >
                {'수정'}
              </Button>
              <Button
                variant={'contained'}
                color={'error'}
                size={'small'}
                sx={{
                  width: '47%',
                  padding: '0.6rem',
                  marginTop: '1rem',
                  fontSize: '18px',
                }}
                onClick={handleDeleteChild}
              >
                {'삭제'}
              </Button>
            </ButtonContainer>
          </FormContainer>
        )}
      </ModalContent>
    </ModalOverlay>
  );
};

export default ModifyModal;
