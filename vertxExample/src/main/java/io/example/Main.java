package io.example;

import io.example.MyFirstVerticle;
import io.vertx.core.Vertx;

/**
 * Created by chengen on 26/04/2017.
 */
public class Main {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(MyFirstVerticle.class.getName());
    }
}