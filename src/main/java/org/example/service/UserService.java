package org.example.service;

import org.example.dto.ProfileDTO;
import org.example.repository.ProfileRepository;
import org.example.utils.ScannerUtils;

public class UserService {

    ScannerUtils scanner = new ScannerUtils();
    ProfileRepository profileRepository=new ProfileRepository();



    public ProfileDTO login(ProfileDTO profileDTO) {
        ProfileDTO profile = profileRepository.login(profileDTO);


        return profile;

    }

    public boolean registration(ProfileDTO profile) {
        boolean result = profileRepository.registration(profile);
        return result;
    }
}
