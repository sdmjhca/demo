package com.sdmjhca.jhmi.vert.xDemo;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * @author JHMI on 2017/10/12.
 */
public class MessageCodecTest implements MessageCodec<MyPOJO,MyPOJO>{
    @Override
    public void encodeToWire(Buffer buffer, MyPOJO myPOJO) {

    }

    @Override
    public MyPOJO decodeFromWire(int i, Buffer buffer) {
        return null;
    }

    @Override
    public MyPOJO transform(MyPOJO myPOJO) {
        return null;
    }

    @Override
    public String name() {
        return "sdmjhca.codec";
    }

    @Override
    public byte systemCodecID() {
        return 0;
    }
}
