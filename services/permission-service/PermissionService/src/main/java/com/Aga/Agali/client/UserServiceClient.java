package com.Aga.Agali.client;

import com.Aga.Agali.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceClient {

    @Value("${user.service.url}")
    private String userServiceUrl;

    private final WebClient webClient;

    public UserServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public UserDto getUserByUniqueId(String uniqueId) {
        return webClient
                .get()
                .uri(userServiceUrl + "/api/user/unique/{uniqueId}", uniqueId)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }
}