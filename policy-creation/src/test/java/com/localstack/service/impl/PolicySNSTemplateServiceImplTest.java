package com.localstack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.exception.SNSException;
import com.localstack.model.Policy;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PolicySNSTemplateServiceImplTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PolicySNSTemplateServiceImpl policySNSTemplateServiceImpl;

    @Mock
    private SnsTemplate snsTemplate;


    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(false),
                Arguments.of(true)
        );
    }


    @ParameterizedTest
    @MethodSource("generateData")
    void testSendNotification(boolean exception) throws JsonProcessingException, SNSException {
        // Arrange

        doNothing().when(snsTemplate).sendNotification(Mockito.<String>any(), Mockito.<SnsNotification<Object>>any());
        NullPointerException mockedException = mock(NullPointerException.class);
        if (exception)
            when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenThrow(mockedException);
        else
            when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");
        Policy policy = new Policy();
        policy.setCategory("Category");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        String actualSendNotificationResult = "";
        // Act
        if (exception)
            assertThrows(SNSException.class, () -> policySNSTemplateServiceImpl.sendNotification(policy));
        else {
            actualSendNotificationResult = policySNSTemplateServiceImpl.sendNotification(policy);
            assertEquals("SUCCESSFULLY SEND NOTIFICATION for Name", actualSendNotificationResult);

        }
        // Assert
        verify(objectMapper).writeValueAsString(isA(Object.class));
    }
}
