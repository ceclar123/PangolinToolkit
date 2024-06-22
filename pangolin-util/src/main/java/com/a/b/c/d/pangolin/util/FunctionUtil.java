package com.a.b.c.d.pangolin.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionUtil {
    private FunctionUtil() {
    }

    public static <T, R> List<R> toList(Collection<T> list, Function<T, R> func) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(func)) {
            return Lists.newArrayList();
        }

        return list.stream()
                .filter(Objects::nonNull)
                .map(func)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T, R> List<R> toList(Collection<T> list, Function<T, R> func, Function<T, Boolean> beforeFilter) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(func)) {
            return Lists.newArrayList();
        }

        return list.stream()
                .filter(Objects::nonNull)
                .filter(it -> Objects.isNull(beforeFilter) || Objects.equals(Boolean.TRUE, beforeFilter.apply(it)))
                .map(func)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <T, R> List<R> toList(Collection<T> list, Function<T, R> func, Function<T, Boolean> beforeFilter, Function<R, Boolean> afterFilter) {
        if (CollectionUtils.isEmpty(list) || Objects.isNull(func)) {
            return Lists.newArrayList();
        }

        return list.stream()
                .filter(Objects::nonNull)
                .filter(it -> Objects.isNull(beforeFilter) || Objects.equals(Boolean.TRUE, beforeFilter.apply(it)))
                .map(func)
                .filter(Objects::nonNull)
                .filter(it -> Objects.isNull(afterFilter) || Objects.equals(Boolean.TRUE, afterFilter.apply(it)))
                .collect(Collectors.toList());
    }
}
