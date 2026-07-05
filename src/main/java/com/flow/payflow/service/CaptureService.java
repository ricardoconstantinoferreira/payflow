package com.flow.payflow.service;

import com.flow.payflow.dto.CaptureApiDto;
import com.flow.payflow.dto.CaptureResponseDto;

public interface CaptureService {
    CaptureResponseDto capture(CaptureApiDto dto);
}
