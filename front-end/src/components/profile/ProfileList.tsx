import React, { useState, useEffect } from 'react';
import { getChildren } from 'apis/profileApis';
import ProfileListItem from './ProfileListItem';
import { ProfileCreateBox, ProfileListBox } from './ProfileListStyles';
import CreateModal from './CreateModal';

interface Profile {
  childId: number;
  birth: string;
  gender: string;
  grade: string;
  name: string;
  school: string;
  status: string;
  profileAnimon: { id: number; bodyImagePath: string; maskImagePath: string; name: string };
}

const ProfileList = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [profiles, setProfiles] = useState<Profile[]>([]);

  useEffect(() => {
    handleGetChildren();
  }, []);

  const handleGetChildren = () => {
    getChildren({
      onSuccess: data => {
        setProfiles(data);
      },
    });
  };

  return (
    <ProfileListBox>
      {profiles.map((profile: Profile) => (
        <ProfileListItem
          key={profile.childId}
          childId={profile.childId}
          name={profile.name}
          imgUrl={profile.profileAnimon.bodyImagePath}
          getChildren={handleGetChildren}
        />
      ))}
      {profiles.length < 3 && <ProfileCreateBox onClick={() => setIsModalOpen(true)} />}
      {isModalOpen && (
        <CreateModal onClose={() => setIsModalOpen(false)} getChildren={handleGetChildren} />
      )}
    </ProfileListBox>
  );
};

export default ProfileList;
