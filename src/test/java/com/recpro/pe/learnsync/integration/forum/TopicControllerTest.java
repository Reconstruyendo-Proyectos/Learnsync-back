package com.recpro.pe.learnsync.integration.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.forum.category.CreateCategoryDTO;
import com.recpro.pe.learnsync.dtos.forum.topic.CreateTopicDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("jluyo", "upao2025");
        String authJson = objectMapper.writeValueAsString(request);
        ResultActions resultActions = this.mockMvc
                .perform(post("/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson));
        MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // Extraer el valor del token como una cadena directamente
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getString("token");
    }

    @Test
    void testListTopics() throws Exception {
        mockMvc.perform(get("/topic/list/")
                .param("page", "0"))
                .andExpect(jsonPath("$", hasSize(not(0))))
                .andExpect(jsonPath("$[0].idTopic").value(1))
                .andExpect(jsonPath("$[1].name").value("Advanced Java"))
                .andExpect(jsonPath("$[2].slug").value("spring-framework"))
                .andExpect(jsonPath("$[3].description").value("An introduction to machine learning concepts"));
    }

    @Test
    void createTopic() throws Exception {
        CreateTopicDTO request = new CreateTopicDTO("Formativa I", "Descripcion para Formativa I", "Music");
        String topicJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/topic/create/")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(topicJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idTopic").value(16))
                .andExpect(jsonPath("$.name").value("Formativa I"))
                .andExpect(jsonPath("$.description").value("Descripcion para Formativa I"))
                .andExpect(jsonPath("$.slug").value("formativa-i"))
                .andExpect(jsonPath("$.threads").isEmpty());;
    }

    @Test
    void createTopicWhenTopicNameExists() throws Exception {
        CreateTopicDTO request = new CreateTopicDTO("Advanced Java", "Descripcion para Formativa I", "Music");
        String topicJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/topic/create/")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(topicJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("El tópico Advanced Java ya existe"));
    }

    @Test
    void createTopicWhenCategoryNotExists() throws Exception {
        CreateTopicDTO request = new CreateTopicDTO("Formativa II", "Descripcion para Formativa I", "Musicaaa");
        String topicJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/topic/create/")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(topicJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("La categoria Musicaaa no existe"));
    }

    @Test
    void testCreateTopicWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateTopicDTO request = new CreateTopicDTO(" ", "ESTO ES UNA DESCRIPCION DE MAS DE 50 CARACTERES DE TAMAÑO", null);
        String categoryJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/topic/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("name")))
                .andExpect(jsonPath("$.errors", hasKey("description")))
                .andExpect(jsonPath("$.errors", hasKey("categoryName")))
                .andExpect(jsonPath("$.errors.name", containsInAnyOrder("No es valido un dato con solo espacio en blanco")))
                .andExpect(jsonPath("$.errors.description", containsInAnyOrder("La descripción debe tener un maximo de 50 caracteres")))
                .andExpect(jsonPath("$.errors.categoryName", containsInAnyOrder("No es valido un dato nulo", "Dato vacio", "No es valido un dato con solo espacio en blanco")));
    }

    @Test
    void testGetTopic() throws Exception {
        mockMvc.perform(get("/topic/advanced-java")
                .pathInfo("/advanced-java"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idTopic").value(2))
                .andExpect(jsonPath("$.name").value("Advanced Java"))
                .andExpect(jsonPath("$.description").value("Deep dive into Java programming"))
                .andExpect(jsonPath("$.slug").value("advanced-java"));
    }

    @Test
    void testGetTopicWhenTopicNotExists() throws Exception {
        mockMvc.perform(get("/topic/topic-not-exists")
                .pathInfo("/topic-not-exists"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El tópico Topic Not Exists no existe"));
    }

}
