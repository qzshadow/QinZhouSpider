package cn.scut.qinzhou.spider.queue;

import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Qin on 5/8/2015.
 */
public class UrlHashMap {
    private static HashMap<String,Integer> urlHashMap = new HashMap<>();

    public synchronized static void addElement(String url,Integer level){
        if(!urlHashMap.containsKey(url)) urlHashMap.put(url, level);
    }

    public synchronized static boolean isEmpty(){
        return urlHashMap.isEmpty();
    }

    public static int size(){return urlHashMap.size();}

    public static Integer getLevel(String url){
        return urlHashMap.get(url);
    }
}
