package com.lagou.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CounterMap {
    // 记录最近一分钟的方法耗时
    static HashMap<String, List[]> methodTime = new HashMap<String, List[]>();

    public static void setIndexMap(HashMap<String, List<Long>> indexMap) {
        CounterMap.indexMap = indexMap;
    }

    // 记录5 秒钟内法耗时
    static HashMap<String, List<Long>> indexMap = new HashMap<String, List<Long>>();

    public static HashMap<String, List[]> getMethodTime() {
        return methodTime;
    }

    public static HashMap<String, List<Long>> getIndexMap() {
        return indexMap;
    }

    public static synchronized void initMap(String key) {

        if (!indexMap.containsKey(key)) {
            LinkedList list = new LinkedList();
            List list1 = Collections.synchronizedList(list);
            indexMap.put(key, list1);
        }
        if (!methodTime.containsKey(key)) {
            List[]  lists = new List[12];  // 5 * 12 =  1 分钟
            methodTime.put(key, lists);
        }
    }


}
