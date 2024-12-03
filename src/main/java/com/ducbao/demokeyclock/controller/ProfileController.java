package com.ducbao.demokeyclock.controller;

import com.ducbao.demokeyclock.dto.request.RegistrationRequest;
import com.ducbao.demokeyclock.dto.response.ApiResponse;
import com.ducbao.demokeyclock.dto.response.ProfileResponse;
import com.ducbao.demokeyclock.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/register")
    ApiResponse<ProfileResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.register(registrationRequest))
                .build();
    }

    @GetMapping("/profiles")
    ApiResponse<List<ProfileResponse>> getProfiles() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfiles())
                .build();
    }

    @GetMapping("/my-profiles")
    ApiResponse<ProfileResponse> getMyProfiles() {
        return ApiResponse.<ProfileResponse>builder()
                .result(profileService.getMyProfile())
                .build();
    }
}
