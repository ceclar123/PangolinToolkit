package com.a.b.c.d.pangolin.util.encode;

import com.github.luben.zstd.Zstd;

public class ZstdUtil {
    private ZstdUtil() {
    }

    public static byte[] compress(byte[] input) throws Exception {
        return Zstd.compress(input, Zstd.defaultCompressionLevel());
    }

    public static byte[] decompress(byte[] input) throws Exception {
        long size = Zstd.getFrameContentSize(input);
        return Zstd.decompress(input, (int) size);
    }
}
