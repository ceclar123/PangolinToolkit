package com.a.b.c.d.pangolin.util.encode;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class XZUtil {
    private XZUtil() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             XZCompressorOutputStream gos = new XZCompressorOutputStream(baos);) {
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
             XZCompressorInputStream gis = new XZCompressorInputStream(bais);) {
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
