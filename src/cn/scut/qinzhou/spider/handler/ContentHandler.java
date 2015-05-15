package cn.scut.qinzhou.spider.handler;

import cn.scut.qinzhou.spider.model.FetchedPage;
import cn.scut.qinzhou.spider.model.SpiderParams;
import cn.scut.qinzhou.spider.queue.UrlQueue;

public class ContentHandler {
    public boolean check(FetchedPage fetchedPage) {

        if(fetchedPage.getContent() == null){return false;}
        // 如果抓取的页面包含反爬取内容，则将当前URL放入待爬取队列，以便重新爬取
        else if (isAntiScratch(fetchedPage)) {
            // 根据搜索策略的不同将新的url放在UrlQueue的不同位置
//            if (SpiderParams.METHOD.equals("Depth")) {
//                UrlQueue.addFirstElement(fetchedPage.getUrl(), fetchedPage.getLevel() + 1);
//            }
//            else if (SpiderParams.METHOD.equals("Width"))
//                    UrlQueue.addElement(fetchedPage.getUrl(), fetchedPage.getLevel() + 1);
            return false;
        }

        return true;
    }

    private boolean isStatusValid(int statusCode) {
        if (statusCode >= 200 && statusCode < 400) {
            return true;
        }
        return false;
    }

    private boolean isAntiScratch(FetchedPage fetchedPage) {
        // 403 forbidden
        if ((!isStatusValid(fetchedPage.getStatusCode())) && fetchedPage.getStatusCode() == 403) {
            return true;
        }

        // 页面内容包含的反爬取内容
        if (fetchedPage.getContent().contains("<div>Forbidden</div>")) {
            return true;
        }

        return false;
    }
}
