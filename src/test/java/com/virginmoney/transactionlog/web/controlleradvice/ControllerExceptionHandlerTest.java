package com.virginmoney.transactionlog.web.controlleradvice;

import com.virginmoney.transactionlog.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandlerUnderTest;
    private final String CHRISTMAS_2024_MIDDAY = "2024-12-25T12:00:00.00z";
    private final Clock clock = Clock.fixed(Instant.parse(CHRISTMAS_2024_MIDDAY), ZoneId.of("UTC"));

    @BeforeEach
    void setup() {
        exceptionHandlerUnderTest = new ControllerExceptionHandler(clock);
    }

    private static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of(new HttpClientErrorException(HttpStatus.NOT_FOUND, "Bad query")),
                Arguments.of(new NotFoundException("Not Found"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void handle404s_HandlesExceptions_AndReturnsExpectedResponse(Exception ex) {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/some-uri");
        request.setRemoteAddr("https://some-domain.com");

        WebRequest webRequest = new ServletWebRequest(request);

        final ErrorMessage expectedErrorMessage = ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND)
                .date(ZonedDateTime.now(clock))
                .message(ex.getMessage())
                .description("uri=/some-uri;client=https://some-domain.com")
                .build();

        ErrorMessage response = exceptionHandlerUnderTest.handle404s(ex, webRequest);

        assertThat(response).isEqualTo(expectedErrorMessage);
    }
}