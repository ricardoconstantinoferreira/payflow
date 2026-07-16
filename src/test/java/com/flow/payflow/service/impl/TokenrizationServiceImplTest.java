package com.flow.payflow.service.impl;

import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenrizationServiceImplTest {

    @Test
    void getTokenrizationReturnsDtoWhenWebClientReturns() throws Exception {
        // Mock WebClient and its fluent API
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        TokenrizationResponseDto dto = new TokenrizationResponseDto();
        // configure mocks
        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TokenrizationResponseDto.class)).thenReturn(Mono.just(dto));

        TokenrizationServiceImpl service = new TokenrizationServiceImpl();
        // inject mocked webClient and uri
        Field f = TokenrizationServiceImpl.class.getDeclaredField("webClient");
        f.setAccessible(true);
        f.set(service, webClient);

        Field uriField = TokenrizationServiceImpl.class.getDeclaredField("uri");
        uriField.setAccessible(true);
        uriField.set(service, "http://test");

        TokenrizationDto request = new TokenrizationDto();

        TokenrizationResponseDto result = service.getTokenrization(request);

        assertEquals(dto, result);
    }
}
