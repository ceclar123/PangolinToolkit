package com.a.b.c.d.pangolin.util.encode;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BZip2Util {
    private BZip2Util() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BZip2CompressorOutputStream bzos = new BZip2CompressorOutputStream(baos);) {
            bzos.write(input);
            bzos.flush();
            bzos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(input);
             BZip2CompressorInputStream bzis = new BZip2CompressorInputStream(bais);) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = bzis.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
