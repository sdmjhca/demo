package com.sdmjhca.jhmi.vert.xDemo;

import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

import java.io.*;

/**
 * @author JHMI on 2017/10/12.
 */
public class MessageCodecTest implements MessageCodec<MyPOJO,MyPOJO>{
    @Override
    public void encodeToWire(Buffer buffer, MyPOJO myPOJO) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(buffer);
            o.close();
            buffer.appendBytes(b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MyPOJO decodeFromWire(int pos, Buffer buffer) {
        final ByteArrayInputStream b = new ByteArrayInputStream(buffer.getBytes());
        ObjectInputStream o = null;
        MyPOJO msg = null;
        try {
            o = new ObjectInputStream(b);
            msg = (MyPOJO) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public MyPOJO transform(MyPOJO myPOJO) {
        return myPOJO;
    }

    @Override
    public String name() {
        return "myCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
