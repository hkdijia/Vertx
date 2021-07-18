package com.gotkx.bean;

import lombok.extern.log4j.Log4j2;
import thirdpart.checksum.ByteCheckSum;
import thirdpart.codec.BodyCodec;

@Log4j2
public class GatewayStartup {

    public static void main(String[] args) throws Exception {
        String configFileName = "gateway.xml";

        GatewayConfig config = new GatewayConfig();
        config.initConfig(GatewayStartup.class.getResource("/").getPath()
                + configFileName);
        config.setCs(new ByteCheckSum());
        config.setBodyCodec(new BodyCodec());
        config.startup();
    }

}
