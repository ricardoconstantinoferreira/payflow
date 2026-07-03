package com.flow.payflow.service;

import com.flow.payflow.dto.TokenrizationDto;
import com.flow.payflow.dto.TokenrizationResponseDto;

public interface TokenrizationService {

    TokenrizationResponseDto getTokenrization(TokenrizationDto tokenrizationDto);
}
