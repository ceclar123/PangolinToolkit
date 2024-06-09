package com.a.b.c.d.pangolin.util;

import org.xerial.snappy.Snappy;

public class SnappyUtil {
    private SnappyUtil() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        return Snappy.compress(input);
    }

    public static byte[] decompress(byte[] input) throws Exception {
        return Snappy.uncompress(input);
    }
}
