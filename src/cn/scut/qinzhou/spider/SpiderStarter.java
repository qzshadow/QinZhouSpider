package cn.scut.qinzhou.spider;

import cn.scut.qinzhou.spider.model.SpiderParams;
import cn.scut.qinzhou.spider.model.urlStruct;
import cn.scut.qinzhou.spider.worker.SpiderWorker;
import cn.scut.qinzhou.spider.queue.UrlQueue;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

import static java.lang.Thread.sleep;


public class SpiderStarter {

    public static void main(String[] args) {
        // 初始化配置参数
        initializeParams();

        // 初始化爬取队列
        initializeQueue();

        // 创建worker线程并启动
        for (int i = 1; i <= SpiderParams.WORKER_NUM; i++) {
            new Thread(new SpiderWorker(i)).start();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
     * 设置种子站点
     */
    private static void initializeQueue() {
        UrlQueue.addElement(new urlStruct("http://www.100steps.net",0));

    }
}
