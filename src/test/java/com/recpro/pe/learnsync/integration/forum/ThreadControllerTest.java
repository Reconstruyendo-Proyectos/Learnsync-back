package com.recpro.pe.learnsync.integration.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.forum.thread.CreateThreadDTO;
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
public class ThreadControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("jluyo", "upao2025");
        String authJson = objectMapper.writeValueAsString(request);
        ResultActions resultActions = this.mockMvc
                .perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson));
        MvcResult mvcResult = resultActions.andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        // Extraer el valor del token como una cadena directamente
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getString("token");
    }

    @Test
    void testListThreads() throws Exception {
        mockMvc.perform(get("/api/v1/threads")
                        .param("page", "0"))
                .andExpect(jsonPath("$", hasSize(not(0))))
                .andExpect(jsonPath("$[0].idThread").value(31))
                .andExpect(jsonPath("$[1].title").value("Home Workouts"))
                .andExpect(jsonPath("$[2].message").value("The history and evolution of jazz music."));
    }

    @Test
    void createThread() throws Exception {
        CreateThreadDTO request = new CreateThreadDTO("¿Quien es Antenor Orrego?", "Ayuden que es para mi tarea gente", "physics-fundamentals", null);
        String threadJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/threads")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(threadJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idThread").value(32))
                .andExpect(jsonPath("$.title").value("¿Quien es Antenor Orrego?"))
                .andExpect(jsonPath("$.message").value("Ayuden que es para mi tarea gente"));
    }

    @Test
    void createThreadWhenTopicNotExists() throws Exception {
        CreateThreadDTO request = new CreateThreadDTO("¿Quien es Antenor Orrego?", "Ayuden que es para mi tarea gente", "topic-not-exists", null);
        String threadJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/threads")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(threadJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El tópico Topic Not Exists no existe"));
    }

    @Test
    void testCreateThreadWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateThreadDTO request = new CreateThreadDTO(" ", "", " ", null);
        String threadJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/threads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(threadJson)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("title")))
                .andExpect(jsonPath("$.errors", hasKey("message")))
                .andExpect(jsonPath("$.errors", hasKey("slug")))
                .andExpect(jsonPath("$.errors.title", containsInAnyOrder("No es valido un dato con solo espacio en blanco")))
                .andExpect(jsonPath("$.errors.message", containsInAnyOrder("Dato vacio", "No es valido un dato con solo espacio en blanco")))
                .andExpect(jsonPath("$.errors.slug", containsInAnyOrder("No es valido un dato con solo espacio en blanco")));
    }

    @Test
    void testGetThread() throws Exception {
        mockMvc.perform(get("/api/v1/threads/1")
                        .pathInfo("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idThread").value(1))
                .andExpect(jsonPath("$.title").value("Introduction to Java: Getting Started"))
                .andExpect(jsonPath("$.message").value("This thread is for beginners starting with Java."));
    }

    @Test
    void testGetThreadWhenThreadNotExists() throws Exception {
        mockMvc.perform(get("/api/v1/threads/99999")
                        .pathInfo("/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe el hilo #99999"));
    }
}
