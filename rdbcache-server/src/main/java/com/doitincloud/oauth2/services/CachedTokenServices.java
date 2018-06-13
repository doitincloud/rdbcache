/*
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.doitincloud.oauth2.services;

import com.doitincloud.rdbcache.services.CacheOps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Base64;

@Service
public class CachedTokenServices implements ResourceServerTokenServices {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedTokenServices.class);

    @Autowired
    private CacheOps cacheOps;

    @Value("${oauth2.server_url}")
    private String oauth2ServerUrl;

    @Value("${oauth2.client_id}")
    private String clientId;

    @Value("${oauth2.client_secret}")
    private String clientSecret;

    private RestOperations restTemplate = new RestTemplate();

    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {

        String cacheKey = "remoteAccessToken::" + accessToken;
        Map<String, Object> map = cacheOps.get(cacheKey);

        if (map == null) {

            HttpHeaders headers = new HttpHeaders();

            String creds = clientId + ":" + clientSecret;
            try {
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(creds.getBytes("UTF-8")));
                headers.set("Authorization", basicAuth);
            }
            catch (UnsupportedEncodingException e) {
                throw new InvalidTokenException(e.getMessage());
            }

            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
            formData.add("token", accessToken);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            String url = oauth2ServerUrl + "/oauth/v1/check_token";
            try {
                map = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class).getBody();
            } catch (ResourceAccessException e) {
                throw new InvalidTokenException("failed to check token: " + e.getMessage());
            } catch (Exception e) {
                String message = e.getMessage();
                if ("401 null".equals(message)) {
                    message = "invalid access token";
                }
                throw new InvalidTokenException("failed to check token: " + message);
            }

            if (map == null || map.containsKey("error")) {
                LOGGER.debug("check_token returned error: " + map.get("error"));
                throw new InvalidTokenException(accessToken);
            }

            // gh-838
            if (!Boolean.TRUE.equals(map.get("active"))) {
                LOGGER.debug("check_token returned active attribute: " + map.get("active"));
                throw new InvalidTokenException(accessToken);
            }

            long exp = Long.valueOf(map.get("exp").toString());
            long cacheTTL = exp - System.currentTimeMillis()/1000;
            if (cacheTTL < 0L) {
                LOGGER.debug("token is expired: " + accessToken);
                throw new InvalidTokenException("token is expired: " + accessToken);
            }
            cacheOps.put(cacheKey, map, cacheTTL * 1000);
        } else {
            LOGGER.trace("use cached token info: " + cacheKey);
        }

        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }
}
