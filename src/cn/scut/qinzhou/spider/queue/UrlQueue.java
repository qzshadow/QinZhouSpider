package cn.scut.qinzhou.spider.queue;

import java.util.LinkedList;
import cn.scut.qinzhou.spider.model.urlStruct;

public class UrlQueue {
    private static LinkedList<urlStruct> urlQueue = new LinkedList<urlStruct>();

    public synchronized static void addElement(String url, Integer level) {
        urlQueue.add(new urlStruct(url, level));
    }
    public synchronized static void addElement(urlStruct us){
        urlQueue.add(us);
    }
    public synchronized static void addFirstElement(String url, Integer level) {
        urlQueue.addFirst(new urlStruct(url, level));
    }
    public  synchronized static void addFirstElement(urlStruct us){
        urlQueue.addFirst(us);
    }

    public synchronized static urlStruct outElement() {
        return urlQueue.removeFirst();
    }

    public synchronized static boolean isEmpty() {
        return urlQueue.isEmpty();
    }

    public static int size() {
        return urlQueue.size();
    }

    public static boolean isContains(String url) {
        for(urlStruct us : urlQueue) if (us.url.equals(url)) return true;
        return false;
    }
}
