package io.examples.grpc.greeting;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;


public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Server.class.getName());
    }

    @Override
    public void start() {
        VertxServer server = VertxServerBuilder.forAddress(vertx, "localhost", 8080)
                .addService(new GreeterGrpc.GreeterVertxImplBase() {
                    @Override
                    public void sayHello(HelloRequest request, Future<HelloReply> future) {
                        System.out.println("Hello " + request.getName());
                        future.complete(HelloReply.newBuilder().setMessage(request.getName()).build());
                    }
                }).build();
        server.start(ar -> {
            if (ar.succeeded()) {
                System.out.println("gRPC service started");
            } else {
                System.out.println("Could not start server " + ar.cause().getMessage());
            }
        });
    }
}
