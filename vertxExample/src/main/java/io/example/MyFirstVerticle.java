package io.example;

import io.vertx.core.AbstractVerticle;

/**
 * Created by chengen on 26/04/2017.
 */
public class MyFirstVerticle extends AbstractVerticle {
    public void start() {
        vertx.createHttpServer()
                .requestHandler(req -> {
                    req.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello World!");
                }).listen(8089);
    }
}