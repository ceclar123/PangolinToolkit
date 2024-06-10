package com.a.b.c.d.pangolin.util.encode;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.LZMAOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class LzmaUtil {
    private LzmaUtil() {
    }

    private static LZMA2Options LZMA_OPTION = new LZMA2Options();

    public static byte[] compress(byte[] input) throws Exception {
        int size = input.length;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZMAOutputStream lzmaos = new LZMAOutputStream(baos, LZMA_OPTION, size);) {
            lzmaos.write(input);
            //lzmaos.flush();
            lzmaos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(input);
             LZMAInputStream lzmais = new LZMAInputStream(bais);) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = lzmais.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
