package com.a.b.c.d.pangolin.util.encode;

import org.apache.commons.compress.java.util.jar.Pack200;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class Pack200Util {
    private Pack200Util() {
    }

    private static final Pack200.Packer PACK200_PACKER = Pack200.newPacker();
    private static final Pack200.Unpacker PACK200_UNPACKER = Pack200.newUnpacker();

    public static byte[] compress(byte[] input) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
             JarInputStream jis = new JarInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            PACK200_PACKER.pack(jis, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] decompress(byte[] input) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             JarOutputStream jos = new JarOutputStream(baos);) {
            PACK200_UNPACKER.unpack(bais, jos);
            baos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        }
    }
}

