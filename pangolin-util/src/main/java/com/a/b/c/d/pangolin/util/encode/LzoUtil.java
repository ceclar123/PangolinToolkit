package com.a.b.c.d.pangolin.util.encode;

import org.anarres.lzo.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class LzoUtil {
    private LzoUtil() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        LzoCompressor compressor = LzoLibrary.getInstance().newCompressor(LzoAlgorithm.LZO1X, null);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LzoOutputStream los = new LzoOutputStream(baos, compressor);) {
            los.write(input);
            los.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        LzoDecompressor decompressor = LzoLibrary.getInstance().newDecompressor(LzoAlgorithm.LZO1X, null);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(input);
             LzoInputStream lis = new LzoInputStream(bais, decompressor);) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = lis.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
