package com.ducbao.demokeyclock.service;

import com.ducbao.demokeyclock.dto.request.RegistrationRequest;
import com.ducbao.demokeyclock.dto.response.ProfileResponse;
import com.ducbao.demokeyclock.mapper.ProfileMapper;
import com.ducbao.demokeyclock.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse register(RegistrationRequest request){
        var profile = profileMapper.toProfile(request);
        profile = profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }
}
