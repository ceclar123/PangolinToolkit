package com.a.b.c.d.pangolin.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static String getStackTrace(final Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }
}
