package cn.scut.qinzhou.spider.fetcher;

import cn.scut.qinzhou.spider.model.FetchedPage;
import cn.scut.qinzhou.spider.model.SpiderParams;
import cn.scut.qinzhou.spider.queue.UrlQueue;
import cn.scut.qinzhou.spider.model.urlStruct;
import cn.scut.qinzhou.spider.queue.VisitedUrlQueue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import org.apache.log4j.Logger;

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
    public FetchedPage getContentFromUrlStruct(urlStruct url_level, int thread_num) {
        String content = null;
        int statusCode = 500;
        boolean connect = true;
        boolean download = true;
        try {
            HttpGet getHttp = null;
            try {
                // 创建Get请求，并设置Header
                getHttp = new HttpGet(url_level.url);
                getHttp.setHeader("User-Agent", "QinZhou 201236672478");
            } catch (java.lang.IllegalArgumentException e) {
                connect = false;
            }
//        getHttp.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
            if(!connect){download = false;}
            else{
                HttpResponse response = null;

            try {
                // 获得信息载体
                response = client.execute(getHttp);
                statusCode = response.getStatusLine().getStatusCode();
            } catch (java.net.SocketTimeoutException | javax.net.ssl.SSLPeerUnverifiedException | java.lang.IllegalArgumentException e) {
                connect = false;
            }
            if (!connect) {
                download = false;
            } else {
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        // 转化为文本信息, 设置爬取网页的字符集，防止乱码
                        content = EntityUtils.toString(entity, "UTF-8");
                    }
                } catch (java.lang.NullPointerException e) {
                    download = false;
                }
            }
        }
        }catch (Exception e){
                e.printStackTrace();

                // 因请求超时等问题产生的异常，将URL放回待抓取队列，重新爬取
                if (url_level.level < SpiderParams.MAX_LEVEL && !VisitedUrlQueue.isContains(url_level.url) && !UrlQueue.isContains(url_level.url)) {
                    Log.info(">> Put back url: " + url_level.url);
                    if (SpiderParams.METHOD.equals("Depth"))
                        UrlQueue.addFirstElement(url_level.url, url_level.level + 1);
                    else if (SpiderParams.METHOD.equals("Width"))
                        UrlQueue.addElement(url_level.url, url_level.level + 1);
                }
        } finally {
            if(connect) Log.info(String.format("Spider-%1d %11s %1d", thread_num, "Connecting", 1));
            else if(!connect) Log.info(String.format("Spider-%1d %11s %1d", thread_num, "Connecting", 0));
            if(download) Log.info(String.format("Spider-%1d %11s %1d", thread_num, "Downloading", 1));
            else if(!download) Log.info(String.format("Spider-%1d %11s %1d", thread_num, "Downloading", 0));
        }
        return new FetchedPage(url_level, content, statusCode);
    }
}

