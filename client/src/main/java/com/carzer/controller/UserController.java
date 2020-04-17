package com.carzer.controller;

import com.carzer.service.OAuth2Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
class UserController {

    @Value("${config.oauth2.resourceURI}")
    private String resourceURI;

    private final OAuth2Client oAuth2Client;

    private final OAuth2RestTemplate restTemplate;

    @Autowired
    public UserController(OAuth2Client oAuth2Client, OAuth2RestTemplate restTemplate) {
        this.oAuth2Client = oAuth2Client;
        this.restTemplate = restTemplate;
    }

    /*@RequestMapping("/")
    public JsonNode home() {
        JsonNode node = restTemplate.getForObject(resourceURI, JsonNode.class);
        return node;
    }*/

    @RequestMapping
    public ModelAndView home() {
        String token = oAuth2Client.getOauth2Token(resourceURI);
        System.out.println(token);
        if (!StringUtils.isEmpty(token)) token = "redirect:" + resourceURI + "?access_token=" + token;
        return new ModelAndView(token);
    }

    @RequestMapping("/hello")
    public String hello() {
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("username", "test");
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("password", "test");
        return restTemplate.getAccessToken().toString();
    }

}