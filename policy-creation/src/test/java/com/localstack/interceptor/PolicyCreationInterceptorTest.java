package com.localstack.interceptor;

import com.localstack.exception.PolicyException;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PolicyCreationInterceptorTest {
    @InjectMocks
    private PolicyCreationInterceptor policyCreationInterceptor;

    @Test
    void testPreHandle_givenFoo_whenHttpServletRequestWrapperGetHeaderReturnFoo() throws Exception {
        // Arrange
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("foo");

        // Act and Assert
        assertThrows(PolicyException.class, () -> policyCreationInterceptor.preHandle(request, new Response(), "Handler"));
    }

    @Test
    void testPreHandle_givenHttpsthenReturnTrue() throws Exception {
        // Arrange
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader(Mockito.<String>any())).thenReturn("https://example.org/example");

        // Act
        boolean actualPreHandleResult = policyCreationInterceptor.preHandle(request, new Response(), "Handler");

        // Assert
        assertTrue(actualPreHandleResult);
    }

    @Test
    void testPreHandle_whenMockHttpServletRequest_thenThrowPolicyException() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();

        // Act and Assert
        assertThrows(PolicyException.class, () -> policyCreationInterceptor.preHandle(request, new Response(), "Handler"));
    }
}
