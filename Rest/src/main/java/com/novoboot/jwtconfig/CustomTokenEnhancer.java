package com.novoboot.jwtconfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.manimalang.models.User;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<String , Object>();
        additionalInfo.put("mobile_number", user.getMobileNo());
        additionalInfo.put("username", user.getName());
        additionalInfo.put("id", user.getUserId());
        additionalInfo.put("address", user.getShopAddress());
        additionalInfo.put("email", user.getEmail());
        additionalInfo.put("password", user.getPassword());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}