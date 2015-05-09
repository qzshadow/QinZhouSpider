package cn.scut.qinzhou.spider.model;

/**
 * Created by Qin on 5/8/2015.
 */
public class urlStruct {
    public String url;
    public Integer level;
    public urlStruct(){}

    public urlStruct(String url, Integer level){
        this.url = url;
        this.level = level;
    }
}
