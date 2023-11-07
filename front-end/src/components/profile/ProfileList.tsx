import React, { useState, useEffect } from 'react';
import instance from 'apis/instance';
import ProfileListItem from './ProfileListItem';
import { ProfileCreateBox, ProfileListBox } from './ProfileListStyles';
import CreateModal from './CreateModal';

interface Profile {
  id: number;
  birth: number;
  gender: string;
  grade: number;
  name: string;
  school: string;
  status: string;
  profileAnimon: { id: 0; bodyImagePath: ''; maskImagePath: ''; name: '' };
}

const ProfileList = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [profiles, setProfiles] = useState<Profile[]>([]);

  useEffect(() => {
    getChildren();
  }, []);

  const getChildren = async () => {
    try {
      const response = await instance.get(`/children`);
      setProfiles(response.data.data);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <ProfileListBox>
      {profiles.map(profile => (
        <ProfileListItem
          key={profile.id}
          childId={profile.id}
          name={profile.name}
          imgurl={profile.profileAnimon.bodyImagePath}
          getChildren={getChildren}
        />
      ))}
      {profiles.length < 3 && <ProfileCreateBox onClick={() => setIsModalOpen(true)} />}
      {isModalOpen && (
        <CreateModal onClose={() => setIsModalOpen(false)} getChildren={getChildren} />
      )}
    </ProfileListBox>
  );
};

export default ProfileList;
