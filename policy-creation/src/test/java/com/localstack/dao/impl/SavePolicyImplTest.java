package com.localstack.dao.impl;

import com.localstack.exception.PolicyException;
import com.localstack.model.Policy;
import org.junit.jupiter.api.Test;
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
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SavePolicyImplTest {
    @Mock
    private DynamoDbTable<Policy> dynamoDbTable;

    @InjectMocks
    private SavePolicyImpl savePolicyImpl;

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(false),
                Arguments.of(true)
        );
    }


    @ParameterizedTest
    @MethodSource("generateData")
    void testSavePolicy(boolean exception) throws PolicyException {

        NullPointerException mockedException = mock(NullPointerException.class);
        if (exception) {
            Mockito.doThrow(mockedException).when(dynamoDbTable).putItem(Mockito.<Policy>any());
            assertThrows(PolicyException.class,() -> savePolicyImpl.savePolicy(new Policy("Code", "Name", "Category", "Premium")));
        } else {
            doNothing().when(dynamoDbTable).putItem(Mockito.<Policy>any());
            savePolicyImpl.savePolicy(new Policy("Code", "Name", "Category", "Premium"));
            verify(dynamoDbTable).putItem(isA(Policy.class));
        }


    }

    @Test
    void testUpsert() {
        // Arrange
        doNothing().when(dynamoDbTable).putItem(Mockito.<Policy>any());
        Policy policyTbl = new Policy("Code", "Name", "Category", "Premium");

        // Act
        Policy actualUpsertResult = savePolicyImpl.upsert(policyTbl);

        // Assert
        verify(dynamoDbTable).putItem(isA(Policy.class));
        assertSame(policyTbl, actualUpsertResult);
    }

    @Test
    void testGetPolicy() {
        // Arrange
        Policy buildResult = Policy.builder().category("Category").code("Code").name("Name").premium("Premium").build();
        when(dynamoDbTable.getItem(Mockito.<Key>any())).thenReturn(buildResult);

        // Act
        Optional<Policy> actualPolicy = savePolicyImpl.getPolicy("Code");

        // Assert
        verify(dynamoDbTable).getItem(isA(Key.class));
        Policy getResult = actualPolicy.get();
        assertEquals("Category", getResult.getCategory());
        assertEquals("Code", getResult.getCode());
        assertEquals("Name", getResult.getName());
        assertEquals("Premium", getResult.getPremium());
        assertTrue(actualPolicy.isPresent());
    }

    @Test
    void testDelete() {
        // Arrange
        Policy buildResult = Policy.builder().category("Category").code("Code").name("Name").premium("Premium").build();
        when(dynamoDbTable.deleteItem(Mockito.<Key>any())).thenReturn(buildResult);

        // Act
        String actualDeleteResult = savePolicyImpl.delete("Code");

        // Assert
        verify(dynamoDbTable).deleteItem(isA(Key.class));
        assertEquals("Policy has been deleted", actualDeleteResult);
    }
    @Test
    void testGetAllPolicies() {
        // Arrange
        SdkIterable<Policy> sdkIterable = mock(SdkIterable.class);

        ArrayList<Policy> policyList = new ArrayList<>();
        Stream<Policy> streamResult = policyList.stream();
        when(sdkIterable.stream()).thenReturn(streamResult);
        PageIterable<Policy> pageIterable = mock(PageIterable.class);
        when(pageIterable.items()).thenReturn(sdkIterable);
        when(dynamoDbTable.scan(Mockito.<ScanEnhancedRequest>any())).thenReturn(pageIterable);

        // Act
        List<Policy> actualAllPolicies = savePolicyImpl.getAllPolicies();

        // Assert
        verify(sdkIterable).stream();
        verify(dynamoDbTable).scan(isA(ScanEnhancedRequest.class));
        verify(pageIterable).items();
        assertTrue(actualAllPolicies.isEmpty());
    }
}
