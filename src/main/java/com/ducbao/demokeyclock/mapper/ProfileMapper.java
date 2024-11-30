package com.ducbao.demokeyclock.mapper;

import com.ducbao.demokeyclock.dto.request.RegistrationRequest;
import com.ducbao.demokeyclock.dto.response.ProfileResponse;
import com.ducbao.demokeyclock.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    Profile toProfile(RegistrationRequest profile);
    ProfileResponse toProfileResponse(Profile profile);
}
