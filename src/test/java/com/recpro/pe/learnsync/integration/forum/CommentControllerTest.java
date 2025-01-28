package com.recpro.pe.learnsync.integration.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.forum.comment.CreateCommentDTO;
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
public class CommentControllerTest {
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
    void testListComments() throws Exception {
        mockMvc.perform(get("/comment/list/")
                        .param("page", "0"))
                .andExpect(jsonPath("$", hasSize(not(0))))
                .andExpect(jsonPath("$[0].idComment").value(1))
                .andExpect(jsonPath("$[1].message").value("This is comment 2"));
    }

    @Test
    void createComment() throws Exception {
        CreateCommentDTO request = new CreateCommentDTO("Esto es un comentario", "jluyo", 1);
        String commentJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/comment/create/")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idComment").value(71))
                .andExpect(jsonPath("$.message").value("Esto es un comentario"));
    }

    @Test
    void createCommentWhenUserNotExists() throws Exception {
        CreateCommentDTO request = new CreateCommentDTO("Esto es un comentario", "USER_NOT_EXISTS", 1);
        String commentJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/comment/create/")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El usuario USER_NOT_EXISTS no fue encontrado"));
    }

    @Test
    void createCommentWhenThreadNotExists() throws Exception {
        CreateCommentDTO request = new CreateCommentDTO("Esto es un comentario", "jluyo", 99999);
        String commentJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/comment/create/")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No existe el hilo #99999"));
    }

    @Test
    void testCreateCommentWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateCommentDTO request = new CreateCommentDTO(" ", "", null);
        String commentJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/comment/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("message")))
                .andExpect(jsonPath("$.errors", hasKey("username")))
                .andExpect(jsonPath("$.errors", hasKey("idThread")))
                .andExpect(jsonPath("$.errors.message", containsInAnyOrder("No es valido un dato con solo espacio en blanco")))
                .andExpect(jsonPath("$.errors.username", containsInAnyOrder("No es valido un dato con solo espacio en blanco", "Dato vacio")))
                .andExpect(jsonPath("$.errors.idThread", containsInAnyOrder("Dato vacio")));
    }
}
