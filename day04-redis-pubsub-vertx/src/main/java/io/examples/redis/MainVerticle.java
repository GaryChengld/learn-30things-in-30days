package io.examples.redis;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
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
