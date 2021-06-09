package com.devsuperior.bds02.controllers;

import com.devsuperior.bds02.util.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerDataIntegrityIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    private String adminUsername;

    private String adminPassword;

    private String accessToken;

    @BeforeEach
    public void setup() throws Exception {
        adminUsername = "maria@gmail.com";
        adminPassword = "123456";

        accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
    }

    @Test
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        Long dependentId = 1L;

        ResultActions result =
                mockMvc.perform(delete("/cities/{id}", dependentId)
                        .header("Authorization", "Bearer" + accessToken));

        result.andExpect(status().isBadRequest());
    }

}
