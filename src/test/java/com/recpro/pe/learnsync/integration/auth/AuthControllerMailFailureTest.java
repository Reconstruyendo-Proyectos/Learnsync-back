package com.recpro.pe.learnsync.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recpro.pe.learnsync.dtos.auth.user.CreateUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mail-failure")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthControllerMailFailureTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegisterWhenMailException() throws Exception {
        CreateUserDTO request = new CreateUserDTO("testuser", "password123", "test@example.com");
        String userJson = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post("/auth/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isFailedDependency());
    }
}