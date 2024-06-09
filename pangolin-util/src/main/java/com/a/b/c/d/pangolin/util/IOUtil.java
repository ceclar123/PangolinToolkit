package com.a.b.c.d.pangolin.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtil {
    private IOUtil() {
    }

    public static String toString(byte[] input, String charsetName) {
        return IOUtils.toString(input, charsetName);
    }

    public static String toString(byte[] input, Charset charset) {
        return IOUtils.toString(input, charset.name());
    }

    public static String toString(InputStream input, String charsetName) throws IOException {
        return IOUtils.toString(input, charsetName);
    }

    public static String toString(InputStream input, Charset charset) throws IOException {
        return IOUtils.toString(input, charset);
    }
}
