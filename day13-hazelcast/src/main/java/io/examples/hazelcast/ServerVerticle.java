package io.examples.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author Gary Cheng
 */
public class ServerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServerVerticle.class);
    private static final String KEY_PORT = "port";
    private Map myMap;

    @Override
    public void start(Future<Void> startFuture) {
        logger.debug("start http server");
        this.startHttpServer()
                .doAfterSuccess(s -> logger.debug("http server started on port {}", s.actualPort()))
                .subscribe(s -> {
                    Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();
                    HazelcastInstance hz = instances.stream().findFirst().get();
                    myMap = hz.getMap("myMap");
                    logger.debug("map size:{}", myMap.size());
                    startFuture.complete();
                }, startFuture::fail);
    }

    private Single<HttpServer> startHttpServer() {
        Router router = Router.router(vertx);
        router.put("/v1/hazelcast/:key").handler(this::putKey);
        router.get("/v1/hazelcast/:key").handler(this::getKey);
        return vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(this.config().getInteger(KEY_PORT));
    }

    private void putKey(RoutingContext context) {
        String key = context.request().getParam("key");
        context.request().bodyHandler(buffer -> {
            logger.debug("Request body:{}", buffer.toJsonObject());
            String value = buffer.toJsonObject().getString("value");
            logger.debug("put value:{}", value);
            myMap.put(key, value);
            this.keyValueResponse(context, key, value);

        });
    }

    private void getKey(RoutingContext context) {
        String key = context.request().getParam("key");
        logger.debug("Received getKey request, key={}", key);
        String value = (String) myMap.get(key);
        logger.debug("get value:{}", value);
        this.keyValueResponse(context, key, value);
    }

    private void keyValueResponse(RoutingContext context, String key, String value) {
        JsonObject jsonObject = new JsonObject().put(key, value);
        context.response()
                .setStatusCode(200)
                .putHeader("Content-Type", "application/json")
                .end(jsonObject.encode());
    }
}
