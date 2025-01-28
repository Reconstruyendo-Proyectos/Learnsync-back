package com.recpro.pe.learnsync.integration.forum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.forum.category.CreateCategoryDTO;
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
public class CategoryControllerTest {

    @Autowired private MockMvc mockMvc;

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
    void testListCategories() throws Exception {
        mockMvc.perform(get("/category/list/")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(not(0))))
                .andExpect(jsonPath("$[0].idCategory").value(1))
                .andExpect(jsonPath("$[1].name").value("Science"))
                .andExpect(jsonPath("$[2].description").value("Artistic expressions and creativity"));
    }

    @Test
    void testCreateCategory() throws Exception {
        CreateCategoryDTO request = new CreateCategoryDTO("Ciclo I", "Categoria para el ciclo I");
        String categoryJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/category/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.idCategory").value(11))
                .andExpect(jsonPath("$.name").value("Ciclo I"))
                .andExpect(jsonPath("$.description").value("Categoria para el ciclo I"))
                .andExpect(jsonPath("$.topics").isEmpty());
    }

    @Test
    void testCreateCategoryWhenCategoryExists() throws Exception {
        CreateCategoryDTO request = new CreateCategoryDTO("Technology", "Categoria para el ciclo I");
        String categoryJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/category/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .header("Authorization", token))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("La categoría Technology existe"));
    }

    @Test
    void testCreateCategoryWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateCategoryDTO request = new CreateCategoryDTO(" ", "ESTO ES UNA DESCRIPCION DE MAS DE 50 CARACTERES DE TAMAÑO");
        String categoryJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/category/create/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("name")))
                .andExpect(jsonPath("$.errors", hasKey("description")))
                .andExpect(jsonPath("$.errors.name", containsInAnyOrder("No es valido un dato con solo espacio en blanco")))
                .andExpect(jsonPath("$.errors.description", containsInAnyOrder("La descripción tiene un maximo de 50 caracteres")));
    }
}