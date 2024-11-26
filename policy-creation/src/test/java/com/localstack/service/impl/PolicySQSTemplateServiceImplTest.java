package com.localstack.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.exception.SQSException;
import com.localstack.model.Policy;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsSendOptions;
import io.awspring.cloud.sqs.operations.SqsTemplate;
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
import org.springframework.messaging.support.GenericMessage;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PolicySQSTemplateServiceImplTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PolicySQSTemplateServiceImpl policySQSTemplateServiceImpl;

    @Mock
    private SqsTemplate sqsTemplate;


    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(false),
                Arguments.of(true)
        );
    }


    @ParameterizedTest
    @MethodSource("generateData")
    void testCreatePolicy(boolean exception) throws JsonProcessingException, SQSException {
        // Arrange
        UUID messageId = UUID.randomUUID();
        GenericMessage<Object> message = new GenericMessage<>("Payload", new HashMap<>());
        NullPointerException mockedException = mock(NullPointerException.class);
        when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");

        if (exception) {
            doThrow(mockedException).when(sqsTemplate).send(Mockito.<Consumer<SqsSendOptions<Object>>>any());
        } else {
            when(sqsTemplate.send(Mockito.<Consumer<SqsSendOptions<Object>>>any()))
                    .thenReturn(new SendResult<>(messageId, "https://config.us-east-2.amazonaws.com", message, new HashMap<>()));

        }
        Policy policy = new Policy();
        policy.setCategory("Category");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        String actualSaveResult = "";
        if (exception)
            assertThrows(SQSException.class, () -> policySQSTemplateServiceImpl.createPolicy(policy));
        else {
            actualSaveResult = policySQSTemplateServiceImpl.createPolicy(policy);
            assertEquals("SUCCESSFULLY CREATED POLICY Name", actualSaveResult);

        }
    }
}
