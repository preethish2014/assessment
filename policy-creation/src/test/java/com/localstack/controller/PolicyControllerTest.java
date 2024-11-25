package com.localstack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.model.Policy;
import com.localstack.service.PolicySNSTemplateService;
import com.localstack.service.PolicySQSTemplateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PolicyControllerTest {
    @InjectMocks
    private PolicyController policyController;

    @Mock
    private PolicySNSTemplateService policySNSTemplateService;

    @Mock
    private PolicySQSTemplateService policySQSTemplateService;

    @Test
    void testCreatePolicythenStatusIsOk() throws Exception {
        // Arrange
        when(policySQSTemplateService.createPolicy(Mockito.<Policy>any())).thenReturn("Create Policy");

        Policy policy = new Policy();
        policy.setCategory("Category");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        String content = (new ObjectMapper()).writeValueAsString(policy);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/policy/creation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(policyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Create Policy"));
    }

    @Test
    void testCreatePolicy_givenEmptyString_thenStatusFourHundred() throws Exception {
        // Arrange
        when(policySQSTemplateService.createPolicy(Mockito.<Policy>any())).thenReturn("Create Policy");

        Policy policy = new Policy();
        policy.setCategory("");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        String content = (new ObjectMapper()).writeValueAsString(policy);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/policy/creation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(policyController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void testPublishNotification() throws Exception {
        // Arrange
        when(policySNSTemplateService.sendNotification(Mockito.<Policy>any())).thenReturn("Send Notification");

        Policy policy = new Policy();
        policy.setCategory("Category");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        String content = (new ObjectMapper()).writeValueAsString(policy);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/policy/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(policyController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Send Notification"));
    }
}
