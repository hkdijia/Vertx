package com.gotkx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
public class TcpClient {

    public static void main(String[] args) {
        new TcpClient().startConn();
    }

    private Vertx vertx;

    public void startConn(){
        vertx = Vertx.vertx();
        vertx.createNetClient().connect(8091,"127.0.0.1", new ClientConnHandler());
    }

    private class ClientConnHandler implements Handler<AsyncResult<NetSocket>>{

        @Override
        public void handle(AsyncResult<NetSocket> result) {
            if(result.succeeded()){
                NetSocket socket = result.result();
                //  关闭连接处理器
                socket.closeHandler(c -> {
                   log.info("connect to {} close");
                    reconnect();
                });
                //  客户端异常处理器
                socket.exceptionHandler(ex -> {
                    log.error("error exist",ex);
                });

                //  发送消息
                byte[] req = "hello i am client".getBytes(StandardCharsets.UTF_8);
                int bodyLength = req.length;
                Buffer buffer = Buffer.buffer().appendInt(bodyLength).appendBytes(req);
                socket.write(buffer);
            }else {
                // 客户端重连
                log.error("connect to remote to server:127.0.0.1:8091 fail");
                reconnect();
            }
        }

        // 重连
        private void reconnect(){
            vertx.setTimer(1000 * 5, res -> {
                log.info("try reconnect to server");
                vertx.createNetClient().connect(8091,"127.0.0.1", new ClientConnHandler());
            });
        }

    }

}
