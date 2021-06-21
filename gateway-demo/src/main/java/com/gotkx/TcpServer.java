package com.gotkx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class TcpServer {

    public static void main(String[] args) {
        new TcpServer().startServ();
    }

    public void startServ(){
        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new ConnHandler());
        netServer.listen(8091, res -> {
            if(res.succeeded()){
                log.info("gateway startup success at port 8091");
            }else {
                log.error("gateway startup fail", res.cause());
            }
        });
    }

    public class ConnHandler implements Handler<NetSocket>{
        private static final int PACKET_HEADER_LENGTH = 4;

        // 报头
        //  包体
        @Override
        public void handle(NetSocket netSocket) {
            RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);
            parser.setOutput(new Handler<Buffer>() {
                int bodyLength = -1;
                @Override
                public void handle(Buffer buffer) {
                    if(bodyLength == -1){
                        bodyLength = buffer.getInt(0);
                        parser.fixedSizeMode(bodyLength);
                    }else {
                        byte[] bufferBytes = buffer.getBytes();
                        log.info("get msg from client: {}", new String(bufferBytes));
                        // 恢复现场
                        parser.fixedSizeMode(PACKET_HEADER_LENGTH);
                        bodyLength = -1;
                    }
                }
            });
            netSocket.handler(parser);
        }
    }

}
