package br.com.ipet.ordering.infrastructure.exception.response;

import java.time.OffsetDateTime;

public record ExceptionResponse(String message,
                                String detail,
                                OffsetDateTime dateTime) {
}
