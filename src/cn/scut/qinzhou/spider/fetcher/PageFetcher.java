package cn.scut.qinzhou.spider.fetcher;

import cn.scut.qinzhou.spider.model.FetchedPage;
import cn.scut.qinzhou.spider.queue.UrlQueue;
import cn.scut.qinzhou.spider.model.urlStruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.logging.Logger;

public class PageFetcher {
    private static final Logger Log = Logger.getLogger(PageFetcher.class.getName());
    private HttpClient client;

    /**
     * 创建HttpClient实例，并初始化连接参数
     */
    public PageFetcher() {
        // 设置超时时间
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
        HttpConnectionParams.setSoTimeout(params, 10 * 1000);
        client = new DefaultHttpClient(params);
    }

    /**
     * 主动关闭HttpClient连接
     */
    public void close() {
        client.getConnectionManager().shutdown();
    }

    /**
     * 根据url爬取网页内容
     *
     * @param url_level
     * @return
     */
    public FetchedPage getContentFromUrlStruct(urlStruct url_level) {
        String content = null;
        int statusCode = 500;

        // 创建Get请求，并设置Header
        HttpGet getHttp = new HttpGet(url_level.url);
//        getHttp.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
        getHttp.setHeader("User-Agent","QinZhou 201236672478");
        HttpResponse response;

        try {
            // 获得信息载体
            response = client.execute(getHttp);
            statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // 转化为文本信息, 设置爬取网页的字符集，防止乱码
                content = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 因请求超时等问题产生的异常，将URL放回待抓取队列，重新爬取
            Log.info(">> Put back url: " + url_level.url);
            UrlQueue.addFirstElement(url_level);
        }

        return new FetchedPage(url_level, content, statusCode);
    }
}
