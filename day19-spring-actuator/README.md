# Day 19: Spring Boot Actuator - Health check, Metrics and Monitoring

## What is Spring Boot Actuator

Spring Boot Actuator module helps you monitor and manage your Spring Boot application by providing production-ready features like health check-up, auditing, metrics gathering, HTTP tracing etc. All of these features can be accessed over JMX or HTTP endpoints.

Actuator also integrates with external application monitoring systems like Prometheus, Graphite, DataDog, Influx, Wavefront, New Relic and many more.

Spring Boot Actuator supplies several endpoints in order to monitor and interact with spring boot application.

## Create Spring Boot application with Actuator

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47250145-c7a3aa00-d3ea-11e8-90ca-49c2df8146c2.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij.
 
**create application.yml** to point http port to 9080

```yaml
server:
  port: 9080
```  

Start application and open http://localhost:9080/actuator in browser, the browser list all the actuator endpoints exposed over HTTP

```json
{
    "_links": {
        "self": {
            "href": "http://localhost:9080/actuator",
            "templated": false
        },
        "health": {
            "href": "http://localhost:9080/actuator/health",
            "templated": false
        },
        "info": {
            "href": "http://localhost:9080/actuator/info",
            "templated": false
        }
    }
}
```

Check the site health by open URL http://localhost:9080/actuator/health, it displays

```json
{
    "status": "UP"
}
```

The info endpoint (http://localhost:9080/actuator/info) displays general information about your application

By default, only the health and info endpoints are exposed over HTTP. That’s why the /actuator page lists only the health and info endpoints.

## Exposing Actuator Endpoints

Here is how to expose actuator endpoints using application properties

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

Restart the application and goto URL http://localhost:9080/actuator/metrics, it shows

```json
{
    "names": [
        "jvm.memory.max",
        "jvm.gc.memory.promoted",
        "jvm.memory.used",
        "jvm.gc.max.data.size",
        "jvm.gc.pause",
        "jvm.memory.committed",
        "system.cpu.count",
        "logback.events",
        "jvm.buffer.memory.used",
        "jvm.threads.daemon",
        "system.cpu.usage",
        "jvm.gc.memory.allocated",
        "jvm.threads.live",
        "jvm.threads.peak",
        "process.uptime",
        "process.cpu.usage",
        "jvm.classes.loaded",
        "jvm.classes.unloaded",
        "jvm.gc.live.data.size",
        "jvm.buffer.count",
        "jvm.buffer.total.capacity",
        "process.start.time"
    ]
}
```

And if want to display any item can go to URL for example http://localhost:9080/actuator/metrics/jvm.memory.max

```json
{
    "name": "jvm.memory.max",
    "description": "The maximum amount of memory in bytes that can be used for memory management",
    "baseUnit": "bytes",
    "measurements": [
        {
            "statistic": "VALUE",
            "value": 2874146815
        }
    ],
    "availableTags": [
        {
            "tag": "area",
            "values": [
                "heap",
                "nonheap"
            ]
        },
        {
            "tag": "id",
            "values": [
                "Compressed Class Space",
                "PS Survivor Space",
                "PS Old Gen",
                "Metaspace",
                "PS Eden Space",
                "Code Cache"
            ]
        }
    ]
}
```

Following is a list of some super useful actuator endpoints.

|Endpoint ID|Description|
|:---|:---|
|auditevents|Exposes audit events (e.g. auth_success, order_failed) for your application|
|metrics|Shows various metrics information of your application|
|loggers|Displays and modifies the configured loggers|
|logfile|Returns the contents of the log file (if logging.file or logging.path properties are set)|
|httptrace|Displays HTTP trace info for the last 100 HTTP request/response|
|env|Displays current environment properties|
|flyway|Shows details of Flyway database migrations|
|shutdown|Lets you shut down the application gracefully|
|mappings|Displays a list of all @RequestMapping paths|
|scheduledtasks|Displays the scheduled tasks in your application|
|threaddump|Performs a thread dump|
|heapdump|Returns a GZip compressed JVM heap dump|

In applicaiton yamlfile, Use "*" to expose all endpoints

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

Restart application and go to http://localhost:9080/actuator/, it shows

```json
{
    "_links": {
        "self": {
            "href": "http://localhost:9080/actuator",
            "templated": false
        },
        "auditevents": {
            "href": "http://localhost:9080/actuator/auditevents",
            "templated": false
        },
        "beans": {
            "href": "http://localhost:9080/actuator/beans",
            "templated": false
        },
        "health": {
            "href": "http://localhost:9080/actuator/health",
            "templated": false
        },
        "conditions": {
            "href": "http://localhost:9080/actuator/conditions",
            "templated": false
        },
        "configprops": {
            "href": "http://localhost:9080/actuator/configprops",
            "templated": false
        },
        "env": {
            "href": "http://localhost:9080/actuator/env",
            "templated": false
        },
        "env-toMatch": {
            "href": "http://localhost:9080/actuator/env/{toMatch}",
            "templated": true
        },
        "info": {
            "href": "http://localhost:9080/actuator/info",
            "templated": false
        },
        "loggers": {
            "href": "http://localhost:9080/actuator/loggers",
            "templated": false
        },
        "loggers-name": {
            "href": "http://localhost:9080/actuator/loggers/{name}",
            "templated": true
        },
        "heapdump": {
            "href": "http://localhost:9080/actuator/heapdump",
            "templated": false
        },
        "threaddump": {
            "href": "http://localhost:9080/actuator/threaddump",
            "templated": false
        },
        "metrics-requiredMetricName": {
            "href": "http://localhost:9080/actuator/metrics/{requiredMetricName}",
            "templated": true
        },
        "metrics": {
            "href": "http://localhost:9080/actuator/metrics",
            "templated": false
        },
        "scheduledtasks": {
            "href": "http://localhost:9080/actuator/scheduledtasks",
            "templated": false
        },
        "httptrace": {
            "href": "http://localhost:9080/actuator/httptrace",
            "templated": false
        },
        "mappings": {
            "href": "http://localhost:9080/actuator/mappings",
            "templated": false
        }
    }
}
```

## Securing Actuator Endpoints with Spring Security

Actuator endpoints are sensitive and must be secured from unauthorized access. If Spring Security is present in your application, then the endpoints are secured by default using a form-based HTTP basic authentication.

Add spring security to your application using the following dependency in pom.xml

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Add java class AppSecurityConfig

```java
@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
                .anyRequest().hasRole("ACTUATOR")
                .and()
                .httpBasic();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("ACTUATOR")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```

## Customizing an Endpoint

Spring also provides adding custom endpoints if you have any application specific feature that you want to exposure and monitor. 

**Create java class CustomFeatureEndPoint**

```java
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
```

Restart application and go to url http://localhost:9080/actuator/myfeatures, the browser shows

```json
{
  "customFeature":"Hello World!"
}
```

 That's all for today, you can find the complete source code under [this folder](.).



