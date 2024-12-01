package com.ducbao.demokeyclock.service;

import com.ducbao.demokeyclock.dto.request.RegistrationRequest;
import com.ducbao.demokeyclock.dto.response.ProfileResponse;
import com.ducbao.demokeyclock.mapper.ProfileMapper;
import com.ducbao.demokeyclock.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final RestTemplate restTemplate;

    @Value("${idp.url}")
    private String URL_KEYCLOCK;
    @Value(("${idp.client-id}"))
    private String clientId;
    @Value("${idp.client-secret}")
    private String clientSecret;

    private final String URL_EXCHANGE_TOKEN = "realms/ducbao/protocol/openid-connect/token";
    private final String URL = "http://localhost:8180";
    private final String GRANT_TYPE = "client_credentials";
    private final String scope = "openid";

    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse register(RegistrationRequest request){
        String accessTokenClient = exchangeClientToken();
        log.info("AccessToken Client: {}", accessTokenClient);
        // Create account in keycloak


        // exchange client token


        // create user with client token and given info
        // Get userId of keycloak account

        var profile = profileMapper.toProfile(request);
        profile.setUserId("");
        profile = profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }

    private String exchangeClientToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", GRANT_TYPE);
        map.add("scope", scope);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        var response = restTemplate.postForObject(generateUrl(URL, URL_EXCHANGE_TOKEN), request, Map.class);
        return response.get("access_token").toString();
    }

    private String generateUrl(String url, String bodyUrl){
        return String.join("/",url, bodyUrl);
    }
}
