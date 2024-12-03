package com.ducbao.demokeyclock.config;

import org.bson.json.StrictJsonWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Lớp này cho phép chuyển đổi ánh xạ từ role trong realm-role của keycloak sang spring boot
 **/

public class CustomAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private final String REALM_ACCESS = "realm_access";
    private final String PREFIX = "ROLE_";
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmClaims = source.getClaimAsMap(REALM_ACCESS);
        Object roles = realmClaims.get("roles");
        if(roles instanceof List stringRoles) {
            return  ((List<String>) stringRoles).stream()
                    .map(role -> new SimpleGrantedAuthority(String.format("%s%s",PREFIX, role)))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
