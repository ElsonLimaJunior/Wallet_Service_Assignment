package com.elson.wallet.infra.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.elson.wallet.AbstractIntegrationTest;
import com.elson.wallet.infra.adapter.in.web.dto.CreateUserRequestDto;
import com.elson.wallet.infra.adapter.in.web.dto.DepositRequestDto;
import com.elson.wallet.infra.adapter.in.web.dto.JwtResponseDto;
import com.elson.wallet.infra.adapter.in.web.dto.LoginRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional 
class WalletControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterLoginDepositAndCheckBalanceSuccessfully() throws Exception {
        // Registrar um novo usuário
        CreateUserRequestDto registerDto = new CreateUserRequestDto("Test User", "test@example.com", "password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated());

        // Fazer login para obter o token JWT
        LoginRequestDto loginDto = new LoginRequestDto("test@example.com", "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseBody = loginResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponse = objectMapper.readValue(responseBody, JwtResponseDto.class);
        String token = jwtResponse.getToken();

        // Depositar fundos na carteira usando o token
        DepositRequestDto depositDto = new DepositRequestDto(new BigDecimal("500.50"));
        mockMvc.perform(post("/api/wallets/me/deposit")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andExpect(status().isOk());

        // Verificar se o saldo foi atualizado corretamente
        mockMvc.perform(get("/api/wallets/me/balance")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.50))
                .andExpect(jsonPath("$.currency").value("BRL"));
    }
}