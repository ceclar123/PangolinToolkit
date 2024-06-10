package com.a.b.c.d.pangolin.util.encode;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Lz4Util {
    private Lz4Util() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4Compressor compressor = factory.fastCompressor();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZ4BlockOutputStream lzos = new LZ4BlockOutputStream(baos, 1024, compressor);) {
            lzos.write(input);
            lzos.flush();
            lzos.finish();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             LZ4BlockInputStream lzis = new LZ4BlockInputStream(new ByteArrayInputStream(input));) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = lzis.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}
