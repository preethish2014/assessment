package com.localstack.exception.handler;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.exception.ListenerException;
import com.localstack.exception.PolicyException;
import com.localstack.exception.SNSException;
import com.localstack.exception.SQSException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExceptionHandlerTest {
    @InjectMocks
    private ExceptionHandler exceptionHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void testHandleSQSException() throws JsonProcessingException {

        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        SQSException ex = new SQSException("An error occurred");

        exceptionHandler.handleSQSException(ex, new ServletWebRequest(new MockHttpServletRequest()));
    }

    @Test
    void testHandleSNSException() throws JsonProcessingException {
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        SNSException ex = new SNSException("An error occurred");

        exceptionHandler.handleSNSException(ex, new ServletWebRequest(new MockHttpServletRequest()));
    }

    @Test
    void testHandleGenericException_whenExceptionWithFoo() throws JsonProcessingException {
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        Exception ex = new Exception("foo");

        exceptionHandler.handleGenericException(ex, new ServletWebRequest(new MockHttpServletRequest()));
    }

    @Test
    void testHandleMethodArgumentNotValid_thenStatusCodeReturnHttpStatus() throws JsonProcessingException {
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");
        MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(null,
                new BindException("Target", "Object Name"));

        ResponseEntity<String> actualHandleMethodArgumentNotValidResult = exceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException, new ServletWebRequest(new MockHttpServletRequest()));

        verify(objectMapper).setVisibility(eq(PropertyAccessor.FIELD), eq(JsonAutoDetect.Visibility.ANY));
        verify(objectMapper).writeValueAsString(isA(Object.class));
        HttpStatusCode statusCode = actualHandleMethodArgumentNotValidResult.getStatusCode();
        assertEquals("42", actualHandleMethodArgumentNotValidResult.getBody());
        HttpHeaders headers = actualHandleMethodArgumentNotValidResult.getHeaders();
        assertEquals(1, headers.size());
        List<String> getResult = headers.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
        assertEquals(400, actualHandleMethodArgumentNotValidResult.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
    }

    @Test
    void testHandlePolicyException() throws JsonProcessingException {
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");
        PolicyException ex = new PolicyException("An error occurred");

        ResponseEntity<String> actualHandlePolicyExceptionResult = exceptionHandler.handlePolicyException(ex,
                new ServletWebRequest(new MockHttpServletRequest()));

        verify(objectMapper).setVisibility(eq(PropertyAccessor.FIELD), eq(JsonAutoDetect.Visibility.ANY));
        verify(objectMapper).writeValueAsString(isA(Object.class));
        HttpStatusCode statusCode = actualHandlePolicyExceptionResult.getStatusCode();
        assertEquals("42", actualHandlePolicyExceptionResult.getBody());
        HttpHeaders headers = actualHandlePolicyExceptionResult.getHeaders();
        assertEquals(1, headers.size());
        List<String> getResult = headers.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
        assertEquals(401, actualHandlePolicyExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        assertTrue(actualHandlePolicyExceptionResult.hasBody());
    }
    @Test
    void testHandleListenerException() throws JsonProcessingException {
        // Arrange
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        when(objectMapper.writeValueAsString(Mockito.<Object>any())).thenReturn("42");
        ListenerException ex = new ListenerException("An error occurred");

        // Act
        ResponseEntity<String> actualHandleListenerExceptionResult = exceptionHandler.handleListenerException(ex,
                new ServletWebRequest(new MockHttpServletRequest()));

        // Assert
        verify(objectMapper).setVisibility(eq(PropertyAccessor.FIELD), eq(JsonAutoDetect.Visibility.ANY));
        verify(objectMapper).writeValueAsString(isA(Object.class));
        HttpStatusCode statusCode = actualHandleListenerExceptionResult.getStatusCode();
        assertEquals("42", actualHandleListenerExceptionResult.getBody());
        HttpHeaders headers = actualHandleListenerExceptionResult.getHeaders();
        assertEquals(1, headers.size());
        List<String> getResult = headers.get(HttpHeaders.CONTENT_TYPE);
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
        assertEquals(500, actualHandleListenerExceptionResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualHandleListenerExceptionResult.hasBody());
    }

    @Test
    void testHandleHttpMediaTypeNotAcceptableException() throws JsonProcessingException {
        when(objectMapper.setVisibility(Mockito.<PropertyAccessor>any(), Mockito.<JsonAutoDetect.Visibility>any()))
                .thenReturn(objectMapper);
        HttpMediaTypeNotAcceptableException ex = new HttpMediaTypeNotAcceptableException("https://example.org/example");

        exceptionHandler.handleHttpMediaTypeNotAcceptableException(ex, new ServletWebRequest(new MockHttpServletRequest()));
    }
}
