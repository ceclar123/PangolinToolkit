package com.a.b.c.d.pangolin.util;

import java.util.ArrayList;

public class Lists {
    public static <E extends Object> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }
}
