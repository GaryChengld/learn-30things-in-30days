package io.examples.springactuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gary Cheng
 */
@Component
@Endpoint(id = "myfeatures")
public class CustomFeatureEndPoint {
    private Map<String, Object> features = new ConcurrentHashMap<>();

    @ReadOperation
    public Map<String, Object> features() {
        features.put("customFeature", "Hello World!");
        return features;
    }
}
