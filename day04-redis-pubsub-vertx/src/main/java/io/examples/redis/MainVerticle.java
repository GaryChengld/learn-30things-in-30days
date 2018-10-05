package io.examples.redis;

import io.reactivex.Single;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gary Cheng
 */
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
        context.request().endHandler(req -> {
            String message = context.request().getFormAttribute("message");
            if (null != message && message.length() > 0) {
                redisClient.rxPublish(REDIS_CHANNEL, message)
                        .subscribe(id -> logger.debug("message {} published", message), t -> logger.debug(t.getLocalizedMessage()));
            }
            context.response().sendFile("webroot/publish.html");
        });
    }
}
