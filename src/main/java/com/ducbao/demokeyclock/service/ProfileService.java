package com.ducbao.demokeyclock.service;

import com.ducbao.demokeyclock.dto.identity.UserCreationParam;
import com.ducbao.demokeyclock.dto.request.RegistrationRequest;
import com.ducbao.demokeyclock.dto.response.ProfileResponse;
import com.ducbao.demokeyclock.mapper.ProfileMapper;
import com.ducbao.demokeyclock.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final String URL_CREATE_USER = "admin/realms/ducbao/users";
    private final String URL = "http://localhost:8180";
    private final String GRANT_TYPE = "client_credentials";
    private final String scope = "openid";

    public List<ProfileResponse> getAllProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(profileMapper::toProfileResponse).toList();
    }

    public ProfileResponse register(RegistrationRequest request){
        // Step 1: Exchange client token with endpoint client exchange token
        String accessTokenClient = exchangeClientToken();
        log.info("AccessToken Client: {}", accessTokenClient);
        // Step 2: Create account in keycloak
        ResponseEntity<?> response = createUser(request, accessTokenClient);
        String userId = extractUserId(response);
        // Get userId onboard database
        log.info("User ID: {}", userId);
        // exchange client token
        // create user with client token and given info
        // Get userId of keycloak account
        var profile = profileMapper.toProfile(request);
        profile.setUserId(userId);
        profile = profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse getMyProfile(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        var profile = profileRepository.findByUserId(userId).orElse(null);
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

    private ResponseEntity<?> createUser(RegistrationRequest request, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        Map<String, Object> body = new HashMap<>();
        body.put("username", request.getUsername());
        body.put("enabled", true);
        body.put("email", request.getEmail());
        body.put("emailVerified", true);
        body.put("firstName", request.getFirstName());
        body.put("lastName", request.getLastName());
        body.put("credentials", List.of(
                Map.of(
                        "type", "password",
                        "value", request.getPassword(),
                        "temporary", false
                )
        ));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<?> response = restTemplate.exchange(
                generateUrl(URL, URL_CREATE_USER),
                HttpMethod.POST,
                requestEntity,
                Void.class
        );
        return response;
    }

    private String extractUserId(ResponseEntity<?> response){
        String location =  response.getHeaders().get("location").toString();
        String [] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    private String generateUrl(String url, String bodyUrl){
        return String.join("/",url, bodyUrl);
    }
}
