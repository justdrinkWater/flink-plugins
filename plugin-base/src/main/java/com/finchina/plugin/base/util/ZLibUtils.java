package com.finchina.plugin.base.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @Auther: renjianfei
 * @Date: 2020/6/1 17:00
 * @Description:
 */
//ZLib压缩工具
public class ZLibUtils {
    private static final Logger logger = LoggerFactory.getLogger(ZLibUtils.class);
    private ZLibUtils(){

    }

    //压缩直接数组
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();

        try(ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);) {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        }
        compresser.end();
        return output;
    }

    //压缩直接数组
    public static byte[] compress(InputStream data) throws IOException {
        byte[] output = new byte[0];
        final int available = data.available();
        byte[] databyte = new byte[available];
        final int count = data.read(databyte);
        if(available!=count){
            logger.warn("zlib compress something abnormal");
        }
        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(databyte);
        compresser.finish();

        try(ByteArrayOutputStream bos = new ByteArrayOutputStream(available);) {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        compresser.end();
        return output;
    }

    //压缩 字节数组到输出流
    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);

        try {
            dos.write(data, 0, data.length);

            dos.finish();

            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //解压缩 字节数组
    public static byte[] decompress(byte[] data) {
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);


        try ( ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);){
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        }

        decompresser.end();
        return output;
    }

    //解压缩 输入流 到字节数组
    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        try {
            int i = 1024;
            byte[] buf = new byte[i];

            while ((i = iis.read(buf, 0, i)) > 0) {
                o.write(buf, 0, i);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }
}

