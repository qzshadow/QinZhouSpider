package cn.scut.qinzhou.spider.queue;

import java.util.LinkedList;
import cn.scut.qinzhou.spider.model.urlStruct;

public class VisitedUrlQueue {
    //
    private static LinkedList<urlStruct> visitedUrlQueue = new LinkedList<urlStruct>();

    public synchronized static void addElement(String url, Integer level) {
        visitedUrlQueue.add(new urlStruct(url, level));
    }
    public synchronized static void addElement(urlStruct us){
        visitedUrlQueue.add(us);
    }
    public synchronized static void addFirstElement(String url, Integer level) {
        visitedUrlQueue.addFirst(new urlStruct(url, level));
    }
    public synchronized static void addFirstElement(urlStruct us){
        visitedUrlQueue.addFirst(us);
    }

    public synchronized static urlStruct outElement() {
        return visitedUrlQueue.removeFirst();
    }

    public synchronized static boolean isEmpty() {
        return visitedUrlQueue.isEmpty();
    }

    public static int size() {
        return visitedUrlQueue.size();
    }

    public static boolean isContains(String url) {
        for(urlStruct us : visitedUrlQueue) if (us.url.equals(url)) return true;
        return false;
    }
}
