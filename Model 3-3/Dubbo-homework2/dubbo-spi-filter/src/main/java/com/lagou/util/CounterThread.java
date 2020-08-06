package com.lagou.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CounterThread implements Runnable {

    int listIndex = 0;   //游标，记录 methodTime 要填充的位置

    @Override
    public void run() {

        for (Map.Entry<String, List<Long>> entry : CounterMap.getIndexMap().entrySet()) {


            List[] lists = CounterMap.getMethodTime().get(entry.getKey());
            lists[listIndex] = entry.getValue();   //拿到indexmap 中对应方法的数据

            //清空 5秒内数据
            synchronized (CounterMap.getIndexMap()) {
                LinkedList list = new LinkedList();
                List list1 = Collections.synchronizedList(list);
                CounterMap.getIndexMap().put(entry.getKey(), list1);
            }

            // 计算TP90 和 TP99
            List<Long> all = new LinkedList<Long>();
            for (int i = 0; i < lists.length; i++) {
                if (lists[i] != null) {
                    all.addAll(lists[i]);
                }
            }
            Collections.sort(all);   // 排序
            Integer t90_index = all.size()* 9 / 10;
            System.out.println("当前方法:  " + entry.getKey() + " 1分钟内调用的次数："+ all.size());
            System.out.println("TP90:  " + all.get(t90_index));

            Integer t99_index = all.size()* 99 / 100;
            System.out.println("TP99:  " + all.get(t99_index));

        }

        listIndex++;
        if (listIndex > 11) {
            listIndex = 0;
        }

    }
}
