package io.examples.spring.rest.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gary Cheng
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {
    private String uploadDir;
}
