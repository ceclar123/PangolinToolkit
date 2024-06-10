package com.a.b.c.d.pangolin.util.encode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
    private GzipUtil() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gos = new GZIPOutputStream(baos);) {
            gos.write(input);
            gos.flush();
            gos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(input);
             GZIPInputStream gis = new GZIPInputStream(bais);) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = gis.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
