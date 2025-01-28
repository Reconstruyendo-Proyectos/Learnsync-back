package com.recpro.pe.learnsync.integration.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.recpro.pe.learnsync.dtos.auth.auth.AuthRequestDTO;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        CreateUserDTO request = new CreateUserDTO("admin", "newAccount", "josemarialuyocampos@gmail.com");
        String userJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("josemarialuyocampos@gmail.com"))
                .andExpect(jsonPath("$.banDate", nullValue()))
                .andExpect(jsonPath("$.points").value(0))
                .andExpect(jsonPath("$.role.roleName").value("STUDENT"));
    }

    @Test
    void testRegisterWhenUsernameExists() throws Exception {
        CreateUserDTO request = new CreateUserDTO("jluyo", "newAccount", "josemarialuyocampos@gmail.com");
        String userJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("El usuario jluyo existe"));
    }

    @Test
    void testRegisterWhenEmailExists() throws Exception {
        CreateUserDTO request = new CreateUserDTO("new_user", "newAccount", "jluyoc1@upao.edu.pe");
        String userJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("El email ya ha sido usado para la creación de otro usuario"));
    }

    @Test
    void testRegisterWhenAtributtesAreEmptyOrNull() throws Exception {
        CreateUserDTO request = new CreateUserDTO("", " ", "hola");
        String userJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/register/")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasKey("username")))
                .andExpect(jsonPath("$.errors", hasKey("password")))
                .andExpect(jsonPath("$.errors", hasKey("email")))
                .andExpect(jsonPath("$.errors.username", containsInAnyOrder("No es valido un dato con solo espacio en blanco", "Dato vacio")))
                .andExpect(jsonPath("$.errors.password", containsInAnyOrder("No es valido un dato con solo espacio en blanco", "La contraseña debe tener minimo 8 caracteres")))
                .andExpect(jsonPath("$.errors.email", containsInAnyOrder("Formato incorrecto")));
    }

    @Test
    void testActiveAccount() throws Exception {
        mockMvc.perform(get("/auth/confirmation-token/550e8400-e29b-41d4-a716-446655440001")
                        .pathInfo("/550e8400-e29b-41d4-a716-446655440001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Activación de Cuenta</title>")))
                .andExpect(content().string(containsString("<h3 class=\"subtitle\">ACTIVACIÓN DE CUENTA</h3>")))
                .andExpect(content().string(containsString("<p class=\"message\">Se activó la cuenta correctamente.</p>")))
                .andExpect(content().string(containsString("<a href=\"http://localhost:4200/login\" class=\"button\">Iniciar sesión</a>")));
    }

    @Test
    void testActiveAccountWhenTokenNotExists() throws Exception {
        mockMvc.perform(get("/auth/confirmation-token/a-sdf")
                        .pathInfo("/a-sdf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Token no válido"));
    }

    @Test
    void testActiveAccountWhenEmailHasConfirmed() throws Exception {
        mockMvc.perform(get("/auth/confirmation-token/550e8400-e29b-41d4-a716-446655440003")
                        .pathInfo("/550e8400-e29b-41d4-a716-446655440000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Este email ya fue confirmado"));
    }

    @Test
    void testActiveAccountWhenTokenWasExpired() throws Exception {
        mockMvc.perform(get("/auth/confirmation-token/550e8400-e29b-41d4-a716-446655440002")
                        .contentType(MediaType.APPLICATION_JSON)
                        .pathInfo("/550e8400-e29b-41d4-a716-446655440002"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("Token expirado"));
    }

    @Test
    void testLogin() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("jluyo", "upao2025");
        String authJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.user").value("jluyo"))
                .andExpect(jsonPath("$.role").value("[ROLE_ADMIN]"))
                .andExpect(jsonPath("$.token", not(emptyOrNullString())))
                .andExpect(jsonPath("$.token").value(matchesPattern("^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$")));

        String token = mockMvc.perform(post("/auth/login/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String jwtToken = JsonPath.read(token, "$.token");

        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        assertNotNull(decodedJWT);
        assertNotNull(decodedJWT.getSubject());
        assertNotNull(decodedJWT.getClaim("authorities").asString());
    }

    @Test
    void testLoginWhenUserNotExists() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("USER_NOT_EXISTS", "password");
        String authJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/login/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("El usuario USER_NOT_EXISTS no fue encontrado"));
    }

    @Test
    void testLoginWhenPasswordNotMatches() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("jluyo", "PASSWORD_NOT_MATCHES");
        String authJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/auth/login/")
                .content(authJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Contraseña inválida"));
    }


}
