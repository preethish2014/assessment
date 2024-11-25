package com.localstack.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.dao.SavePolicy;
import com.localstack.exception.ListenerException;
import com.localstack.exception.PolicyException;
import com.localstack.model.Policy;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PolicyCreationListenerTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PolicyCreationListener policyCreationListener;

    @Mock
    private SavePolicy savePolicy;

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(false),
                Arguments.of(true)
        );
    }


    @ParameterizedTest
    @MethodSource("generateData")
    void testListen(boolean exception) throws JsonProcessingException, ListenerException, PolicyException {
        // Arrange
        Policy policy = new Policy();
        policy.setCategory("Category");
        policy.setCode("Code");
        policy.setName("Name");
        policy.setPremium("Premium");
        NullPointerException mockedException = mock(NullPointerException.class);
        if (exception) {
            Mockito.when(objectMapper.readValue((String)Mockito.any(),Mockito.any(Class.class))).thenThrow(mockedException);
        } else {
            when(objectMapper.readValue(Mockito.<String>any(), Mockito.<Class<Policy>>any())).thenReturn(policy);
            when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");
            doNothing().when(savePolicy).savePolicy(Mockito.<Policy>any());
        }
        Policy policy2 = new Policy();
        policy2.setCategory("Category");
        policy2.setCode("Code");
        policy2.setName("Name");
        policy2.setPremium("Premium");

        // Act


        if (exception) {
            Assertions.assertThrows(ListenerException.class, () -> policyCreationListener.listen(objectMapper.writeValueAsString(policy2)));
        } else {
            policyCreationListener.listen(objectMapper.writeValueAsString(policy2));
            // Assert that nothing has changed
            verify(objectMapper).readValue(eq("42"), isA(Class.class));
            verify(objectMapper).writeValueAsString(isA(Object.class));
            verify(savePolicy).savePolicy(isA(Policy.class));
        }

    }
}
