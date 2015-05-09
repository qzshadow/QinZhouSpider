package cn.scut.qinzhou.spider.parser;

import cn.scut.qinzhou.spider.model.FetchedPage;
import cn.scut.qinzhou.spider.model.SpiderParams;
import cn.scut.qinzhou.spider.queue.UrlQueue;
import cn.scut.qinzhou.spider.queue.VisitedUrlQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ContentParser {
    public Object parse(FetchedPage fetchedPage) {
        Object targetObject = null;
        Document doc = Jsoup.parse(fetchedPage.getContent());

        // 如果当前页面包含目标数据
        if (containsTargetData(fetchedPage.getUrl(), doc)) {
            // 解析并获取目标数据
            // TODO
        }

        // 将URL放入已爬取队列
        VisitedUrlQueue.addElement(fetchedPage.getUrl(), fetchedPage.getLevel());

        // 根据当前页面和URL获取下一步爬取的URLs
        getUrlsInContents(doc, fetchedPage.getLevel()+1);

        return targetObject;
    }

    private boolean containsTargetData(String url, Document contentDoc) {
        // 通过URL判断
        // TODO
        System.out.println(url);

        // 通过content判断，比如需要抓取class为grid_view中的内容
        if (contentDoc.getElementsByClass("grid_view") != null) {
//            System.out.println(contentDoc.getElementsByClass("grid_view").toString());
            return true;
        }

        return false;
    }
    //根据当前页面URL获取下一步爬取的URL
    private void getUrlsInContents(Document contentDoc,Integer level){
        Elements urls = contentDoc.select("a[href]");
        for(Element url : urls){
            //判断爬取深度是否超出限制
            String addUrl = url.attr("abs:href");
            if(level < SpiderParams.MAX_LEVEL && !visited(addUrl)) {
                // 根据搜索策略的不同将新的url放在UrlQueue的不同位置
                if (SpiderParams.METHOD.equals("Depth")) UrlQueue.addFirstElement(addUrl, level);
                else if (SpiderParams.METHOD.equals("Width")) UrlQueue.addElement(addUrl, level);
            }
        }
    }
    private boolean visited(String addUrl){
        return VisitedUrlQueue.isContains(addUrl);
    }
}
