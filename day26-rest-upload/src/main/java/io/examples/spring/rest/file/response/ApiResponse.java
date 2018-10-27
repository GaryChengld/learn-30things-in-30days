package io.examples.spring.rest.file.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic Api Response
 *
 * @author Gary Cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    public static final String TYPE_MESSAGE = "Message";
    public static final String TYPE_ERROR = "Error";

    private String type;
    private String message;

    public static ApiResponse message(String message) {
        return new ApiResponse(TYPE_MESSAGE, message);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(TYPE_ERROR, message);
    }
}
