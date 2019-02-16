package com.carzer.service;

import com.carzer.model.DemoBaseClientDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DemoBaseClientDetailsDAO {


    public DemoBaseClientDetails findByClientId(String clientId){
        if(clientId.equals("trusted")) {
            DemoBaseClientDetails trustedDetails = new DemoBaseClientDetails();
            trustedDetails.setClientId("trusted");
            trustedDetails.setClientSecret("secret");
            trustedDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_TRUSTED_CLIENT")));
            trustedDetails.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "password", "authorization_code", "refresh_token", "implicit"));
            trustedDetails.setScope(Arrays.asList("read", "write"));
            trustedDetails.setAccessTokenValiditySeconds(30 * 60);
            trustedDetails.setRefreshTokenValiditySeconds(3 * 30 * 60);
            return trustedDetails;
        }
        return null;
    }
}
