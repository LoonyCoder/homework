package com.lagou.util;

import java.util.concurrent.atomic.AtomicInteger;

public class TimeCounter {
    static AtomicInteger atomicInteger= new AtomicInteger(5);

    public static AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }
}
