package cn.scut.qinzhou.spider;

import cn.scut.qinzhou.spider.model.SpiderParams;
import cn.scut.qinzhou.spider.model.urlStruct;
import cn.scut.qinzhou.spider.worker.SpiderWorker;
import cn.scut.qinzhou.spider.queue.UrlQueue;
import sun.security.provider.ConfigFile;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;


public class SpiderStarter {

    public static void main(String[] args) {
        // 初始化配置参数
        initializeParams();

        // 初始化爬取队列
        initializeQueue();

        // 创建worker线程并启动
        for (int i = 1; i <= SpiderParams.WORKER_NUM; i++) {
            new Thread(new SpiderWorker(i)).start();
        }
    }

    /**
     * 初始化配置文件参数
     */
    private static void initializeParams() {
        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream("conf/spider.properties"));
            Properties properties = new Properties();
            properties.load(in);

            // 从配置文件中读取参数
            SpiderParams.WORKER_NUM = Integer.parseInt(properties.getProperty("spider.threadNum"));
            SpiderParams.DEYLAY_TIME = Integer.parseInt(properties.getProperty("spider.fetchDelay"));
            SpiderParams.MAX_LEVEL = Integer.parseInt(properties.getProperty("spider.fetchDepth"));
            SpiderParams.FETCHTYPE = Arrays.asList(properties.getProperty("spider.fetchType").split("\\s*,\\s*"));
            SpiderParams.MAX_SIZE = Integer.parseInt(properties.getProperty("spider.fetchSizeMax"));
            SpiderParams.MIN_SIZE = Integer.parseInt(properties.getProperty("spider.fetchSizeMin"));
            SpiderParams.METHOD = properties.getProperty("spider.fetchMethod");

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 准备初始的爬取链接
     */
    private static void initializeQueue() {
        // 例如，需要抓取豆瓣TOP 250的电影信息，根据链接规则生成URLs放入带抓取队列
//        for (int i = 0; i < 250; i += 25) {
//            UrlQueue.addElement("http://movie.douban.com/top250?start=" + i);
//        }

        UrlQueue.addElement(new urlStruct("http://www.100steps.net/index.php?option=com_content&view=categories&id=13&Itemid=174",0));
        UrlQueue.addElement(new urlStruct("http://www.100steps.net",0));

//        System.out.println(UrlQueue.size());
    }
}
