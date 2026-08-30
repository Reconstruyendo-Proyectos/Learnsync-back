package com.recpro.pe.learnsync.services.integrations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.image.ImageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImgurService {


    private final WebClient webClient = WebClient.builder().build();
    private final ObjectMapper objectMapper;

    @Value("${imgur.api.url}")
    private String imgurUrl;

    @Value("${imgur.bearer.token}")
    private String accessToken;

    public ImageResponseDTO uploadImage(MultipartFile file) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", file.getResource());
            body.add("type", "image");

            Mono<String> responseMono = webClient.post()
                    .uri(imgurUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = responseMono.block();

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.path("success").asBoolean()) {
                String url = jsonNode.path("data").path("link").asText();
                String deleteHash = jsonNode.path("data").path("deletehash").asText();

                return new ImageResponseDTO(url, deleteHash);
            } else {
                throw new RuntimeException("Error en Imgur: " + jsonNode.path("data").path("error").asText());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Void deleteImage(String deleteHash) {
        if (deleteHash == null || deleteHash.isEmpty()) {
            throw new RuntimeException("Delete Hash inválida: ");
        }

        webClient.delete()
                .uri(imgurUrl + "/{deleteHash}", deleteHash)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return null;
    }
}
