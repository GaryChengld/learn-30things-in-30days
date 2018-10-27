package io.examples.spring.rest.file.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gary Cheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private String fileName;
    private String downloadUri;
    private String fileType;
    private long fileSize;
}
