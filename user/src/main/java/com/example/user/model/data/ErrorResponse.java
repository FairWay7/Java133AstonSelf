package com.example.user.model.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse extends RepresentationModel<ErrorResponse> {

    @Schema(description = "Временная метка")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP статус код")
    private int status;

    @Schema(description = "Тип ошибки")
    private String error;

    @Schema(description = "Сообщение об ошибке")
    private String message;

    @Schema(description = "Детали ошибок")
    private Map<String, String> details;

    @Schema(description = "Путь запроса")
    private String path;

    public static ErrorResponse of(Throwable throwable, HttpStatusCode status, String path) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.toString())
            .message(throwable.getMessage())
            .path(path)
            .build();
    }
}
