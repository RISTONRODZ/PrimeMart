package org.riston.ecommerce.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public record ApiResponseDto<T>(
        @Schema(description = "Indicates if the request was successful", example = "true")
        boolean success,
        @Schema(description = "A human-readable message", example = "Operation successful")
        String message,
        @Schema(description = "The payload data of the response")
        T data
) {
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>(false, message, null);
    }
}