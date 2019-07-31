package com.carzer.security;

import com.carzer.service.DemoBaseClientDetailsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${config.oauth2.resourceIds}")
    private String resourceIds;

    @Value("${config.oauth2.privateKey}")
    private String privateKey;

    @Value("${config.oauth2.publicKey}")
    private String publicKey;

    private final AuthenticationManager authenticationManager;

    private final DemoBaseClientDetailsDAO demoBaseClientDetailsDAO;

    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager, DemoBaseClientDetailsDAO demoBaseClientDetailsDAO) {
        this.authenticationManager = authenticationManager;
        this.demoBaseClientDetailsDAO = demoBaseClientDetailsDAO;
    }

    @Bean
    public JwtAccessTokenConverter tokenEnhancer() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(privateKey);
        accessTokenConverter.setVerifierKey(publicKey);
        return accessTokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenEnhancer());
    }

    /**
     * Defines the security constraints on the token endpoints /oauth/token_key and /oauth/check_token
     * Client credentials are required to access the endpoints
     *
     * @param oauthServer AuthorizationServerSecurityConfigurer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("isAnonymous() || hasRole('ROLE_TRUSTED_CLIENT')") // permitAll()
                .checkTokenAccess("hasRole('TRUSTED_CLIENT')"); // isAuthenticated()
    }

    /**
     * Defines the authorization and token endpoints and the token services
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints

                // Which authenticationManager should be used for the password grant
                // If not provided, ResourceOwnerPasswordTokenGranter is not configured
                .authenticationManager(authenticationManager)

                // Use JwtTokenStore and our jwtAccessTokenConverter
                .tokenStore(tokenStore())
                .accessTokenConverter(tokenEnhancer())
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(myClientDetailsService());
    }

    @Bean
    public ClientDetailsService myClientDetailsService() {
        return demoBaseClientDetailsDAO::findByClientId;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}