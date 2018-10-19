package io.examples.zipkin.service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ZipkinService1Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinService1Application.class, args);
    }
}

@RestController
@RequestMapping(value = "/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
class ZipkinController {
    private static final Logger logger = LoggerFactory.getLogger(ZipkinController.class);

    @Bean
    public AlwaysSampler alwaysSampler() {
        return new AlwaysSampler();
    }


    @GetMapping(value = "/service1")
    @ResponseBody
    public Result1 service1() {
        logger.info("Inside zipkin Service 1..");
        Result1 result1 = new Result1();
        result1.setMessage("service1");
        return result1;
    }
}
