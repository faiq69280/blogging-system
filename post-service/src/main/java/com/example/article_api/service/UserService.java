package com.example.article_api.service;

import com.example.article_api.domain.UserModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {
    private WebClient webClient;

    public UserService(@Qualifier("userServiceClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public UserModel getUserInfo(String userId) {
       return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_USER_INFO_ENDPOINT)
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .bodyToMono(UserModel.class)
                .block();
    }

    private static final String  GET_USER_INFO_ENDPOINT = "user/getUserInfo";
}
