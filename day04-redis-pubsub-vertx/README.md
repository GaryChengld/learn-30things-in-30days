# Day 4: Redis message publish/subscribe programming on Vert.x

Redis is a key/value in memory data store, but it also has an utility which enable you to do quick messaging and communication between processes. There’s plenty of other messaging systems out there, but Redis is worth a look too.

Today I will create a simple Redis message publish/subscribe application by Java on Vert.x.

Let's take a look on Redis publish and subscribe command first.

 - SUBSCRIBE command listens to a channel
 - PUBLISH pushes a message into a channel
 
 Start Redis server and open 2 Redis-cli programs, in first Redis client, execute SUBSCRIBE command to listen my-channel
 ```
 redis 127.0.0.1:6379> subscribe my-channel
 Reading messages... (press Ctrl-C to quit)
 1) "subscribe"
 2) "my-channel"
 3) (integer) 1
 ```
 Please note that redis-cli will not accept any commands once in subscribed mode and can only quit the mode with Ctrl-C.
 
 In second Redis client, publish a message to my-channel
 ```
 redis 127.0.0.1:6379> publish my-channel "hello"
 (integer) 1
 redis 127.0.0.1:6379>
 ```
 The message "hello" is received in first Redis client
 ```
 redis 127.0.0.1:6379> subscribe my-channel
 Reading messages... (press Ctrl-C to quit)
 1) "subscribe"
 2) "my-channel"
 3) (integer) 1
 1) "message"
 2) "my-channel"
 3) "hello"
 ```
 
 Now I start create a Vert.x based java program to publish/subscribe message through Redis.
 
 Create a maven project with following dependencies
 ```xml
   <dependencyManagement>
     <dependencies>
       <dependency>
         <groupId>io.vertx</groupId>
         <artifactId>vertx-dependencies</artifactId>
         <version>${vertx.version}</version>
         <type>pom</type>
         <scope>import</scope>
       </dependency>
     </dependencies>
   </dependencyManagement>
 
   <dependencies>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-core</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-web</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-rx-java2</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-redis-client</artifactId>
     </dependency>
     <dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-classic</artifactId>
       <version>1.2.3</version>
     </dependency>
   </dependencies>
 ```
 
 First step, create a simple program which can publish messages to Redis channel only
 
 Configuration file - config.json
 ```json
 {
   "redis": {
     "host": "127.0.0.1",
     "port": 6379
   }
 }
 ```
 Create Vert.x verticle
 ```java
 public class MainVerticle extends AbstractVerticle {
     private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
 
     private static final String KEY_REDIS = "redis";
     private static final String KEY_HOST = "host";
     private static final String KEY_PORT = "port";
     private static final String REDIS_CHANNEL = "my-channel";
 
     // Convenience method so you can run it in IDE
     public static void main(String[] args) {
         JsonObject config = new JsonObject();
         config.put(KEY_REDIS, new JsonObject().put(KEY_HOST, "127.0.0.1").put(KEY_PORT, 6379));
         Vertx vertx = Vertx.vertx();
         vertx.rxDeployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setConfig(config))
                 .subscribe(id -> logger.debug("MainVerticle deployed successfully with deployment ID {}", id),
                         ex -> {
                             logger.error(ex.getLocalizedMessage());
                             vertx.close();
                         });
     }
 
     @Override
     public void start() {
         JsonObject redisConfig = this.config().getJsonObject(KEY_REDIS);
         RedisOptions config = new RedisOptions().setHost(redisConfig.getString(KEY_HOST)).setPort(redisConfig.getInteger(KEY_PORT));
         RedisClient redis = RedisClient.create(vertx, config);
         vertx.setPeriodic(5000, id -> redis.rxPublish(REDIS_CHANNEL, "message from Vert.x")
                 .subscribe(l -> logger.debug("Message sent"), t -> logger.debug(t.getLocalizedMessage()))
         );
     }
 }
 ```
 Once the verticle started, it sends message to Redis channel every 5 second, execute this program, you can see the messages are displayed in Redis client which subscribe "my-channel" channel.
   <img width="660" src="https://user-images.githubusercontent.com/3359299/46514294-1538e800-c82b-11e8-90cf-1ef431605ad2.PNG" />
 
 Next step, I will create a web page which can publish message to Redis channel
 
 Change config.json
 ```
 {
   "redis": {
     "host": "127.0.0.1",
     "port": 6379
   },
   "web": {
     "port": 9080
   }
 }
 ```
 
 Create HTML page pubish.html
 ```html
 <!DOCTYPE html>
 <html lang="en">
 <head>
     <meta charset="UTF-8">
     <title>Redis Message Publisher</title>
 </head>
 <h2>Redis message publisher</h2>
 <body>
 <form id="publishForm" action="/publish" method="POST">
     Message: <input type="text" name="message" id="message"/><br><br>
     <input type='submit' name='Submit' value='Submit' />
 </form>
 </body>
 </html>
 ```
 
 Then change MainVerticle to
 ```java
 public class MainVerticle extends AbstractVerticle {
     private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
 
     private static final String KEY_REDIS = "redis";
     private static final String KEY_WEB = "web";
     private static final String KEY_HOST = "host";
     private static final String KEY_PORT = "port";
     private static final String REDIS_CHANNEL = "my-channel";
     private RedisClient redisClient;
 
     // Convenience method so you can run it in IDE
     public static void main(String[] args) {
         JsonObject config = new JsonObject();
         config.put(KEY_REDIS, new JsonObject().put(KEY_HOST, "127.0.0.1").put(KEY_PORT, 6379));
         config.put(KEY_WEB, new JsonObject().put(KEY_PORT, 9080));
         Vertx vertx = Vertx.vertx();
         vertx.rxDeployVerticle(MainVerticle.class.getName(), new DeploymentOptions().setConfig(config))
                 .subscribe(id -> logger.debug("MainVerticle deployed successfully with deployment ID {}", id),
                         ex -> {
                             logger.error(ex.getLocalizedMessage());
                             vertx.close();
                         });
     }
 
     @Override
     public void start(Future<Void> startFuture) {
         JsonObject redisConfig = this.config().getJsonObject(KEY_REDIS);
         RedisOptions redisOptions = new RedisOptions()
                 .setHost(redisConfig.getString(KEY_HOST))
                 .setPort(redisConfig.getInteger(KEY_PORT));
         redisClient = RedisClient.create(vertx, redisOptions);
         logger.debug("start http server");
         this.startHttpServer()
                 .doAfterSuccess(s -> logger.debug("http server started on port {}", s.actualPort()))
                 .subscribe(s -> startFuture.complete(), startFuture::fail);
     }
 
     private Single<HttpServer> startHttpServer() {
         JsonObject webConfig = this.config().getJsonObject(KEY_WEB);
         Router router = Router.router(vertx);
         router.get("/publish").handler(this::publishPageHandler);
         router.post("/publish").handler(this::publishMessageHandler);
         return vertx.createHttpServer()
                 .requestHandler(router::accept)
                 .rxListen(webConfig.getInteger(KEY_PORT));
     }
 
     private void publishPageHandler(RoutingContext context) {
         context.response().sendFile("webroot/publish.html");
     }
 
     private void publishMessageHandler(RoutingContext context) {
         context.request().setExpectMultipart(true);
         context.request().endHandler(v -> {
             String message = context.request().getFormAttribute("message");
             if (null != message && message.length() > 0) {
                 redisClient.rxPublish(REDIS_CHANNEL, message)
                         .subscribe(id -> logger.debug("message {} published", message), t -> logger.debug(t.getLocalizedMessage()));
             }
             context.response().sendFile("webroot/publish.html");
         });
     }
 }
 ```
 
 ### Build
 ```
 mvn clean package
 ```
 ### Start
 ```
java -jar target\redis-pubsub-0.0.1-SNAPSHOT-fat.jar -conf src\conf\config.json
 ```

Now you can see a http server started on port 9080
```
E:\Workspaces\learn-30things-in-30days\day04-redis-pubsub-vertx>java -jar target\redis-pubsub-0.0.1-SNAPSHOT-fat.jar -co
nf src\conf\config.json
00:35:23.537 [vert.x-eventloop-thread-0] DEBUG io.examples.redis.MainVerticle - start http server
Oct 05, 2018 12:35:25 AM io.vertx.core.impl.BlockedThreadChecker
WARNING: Thread Thread[vert.x-eventloop-thread-0,5,main] has been blocked for 2003 ms, time limit is 2000
Oct 05, 2018 12:35:25 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
00:35:25.703 [vert.x-eventloop-thread-0] DEBUG io.examples.redis.MainVerticle - http server started on port 9080
```

Open url http://localhost:9080/publish in browser

  <img width="660" src="https://user-images.githubusercontent.com/3359299/46516368-4b7c6480-c837-11e8-9618-af11c29f96ef.PNG" />

Enter the message in text box, then click submit button, the message will be displayed in Redis client
```
redis 127.0.0.1:6379> subscribe my-channel
Reading messages... (press Ctrl-C to quit)
1) "message"
2) "my-channel"
3) "the message"
1) "message"
2) "my-channel"
3) "Second message"
```

This is all for today, you can find the complete source code under [this folder](src).