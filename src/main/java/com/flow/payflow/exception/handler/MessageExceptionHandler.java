package com.flow.payflow.exception.handler;

import com.flow.payflow.exception.dto.MessageExceptionDto;
import com.flow.payflow.exception.MessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MessageExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(MessageException.class)
    public MessageExceptionDto handler(MessageException e) {
        return new MessageExceptionDto(
                e.getMessage(),
                e.getStatus()
        );
    }
}
