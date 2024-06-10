package com.a.b.c.d.pangolin.util.encode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class DeflateUtil {
    private DeflateUtil() {
    }


    public static byte[] compress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DeflaterOutputStream dos = new DeflaterOutputStream(baos);) {
            dos.write(input);
            dos.flush();
            dos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(input);
             InflaterInputStream dis = new InflaterInputStream(bais);) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = dis.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
