package com.crawler.example.timer;

import com.crawler.example.crawler.*;
import com.crawler.example.service.ResultService;
import com.crawler.example.storage.ListResultStorage;
import com.crawler.example.storage.ResultStorage;
import com.crawler.example.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.security.provider.ConfigFile;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * app上新闻，微博抓取
 */
@Component
public class NewsTimer {
    @Autowired
    ResultStorage resultStorage;
    @Autowired
    ListResultStorage listResultStorage;
    @Autowired
    ResultService resultService;

//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void crawlerSina() throws Exception {
        String url_page_one="http://cj.sina.com.cn/k/api/article/lists_by_column?sort_id=2&page=1";
        String url_page_two="http://cj.sina.com.cn/k/api/article/lists_by_column?sort_id=2&page=2";
        String url_A_one="http://cj.sina.com.cn/k/api/article/lists_by_column?sort_id=3&page=1";
        String url_A_two="http://cj.sina.com.cn/k/api/article/lists_by_column?sort_id=3&page=2";
        Spider spider=Spider.create(new SinaA()).addUrl(url_page_one,url_page_two,url_A_one,url_A_two)
                .addPipeline(resultStorage).thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间："+ TimeUtil.getDateTime());
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 5 */1 * * ?")
    public void crawlerZQRB() throws Exception {
        String url_economy="http://www.zqrb.cn/finance/hongguanjingji/index.html";
        String url_direction="http://www.zqrb.cn/finance/hangyedongtai/index.html";
        String url_international="http://www.zqrb.cn/finance/guojijingji/index.html";
        Spider spider=Spider.create(new ZQRB()).addUrl(url_economy,url_direction,url_international)
                .addPipeline(resultStorage).thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间："+ TimeUtil.getDateTime());
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 10 */1 * * ?")
    public void crawlerZQSBW() throws Exception {
        String dapan="http://stock.stcn.com/dapan/index.shtml";
        String zhuli="http://stock.stcn.com/zhuli/index.shtml";
        String bankuai="http://stock.stcn.com/bankuai/index.shtml";
        Spider spider=Spider.create(new ZQSBW()).addUrl(dapan,zhuli,bankuai)
                .addPipeline(resultStorage).thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间："+ TimeUtil.getDateTime());
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 15 */1 * * ?")
    public void crawlerWeiBo() throws Exception {
        List<String> urls=resultService.selectWeiBoUrl();
        Spider spider=Spider.create(new SinaWeiBo())
                .addPipeline(listResultStorage).thread(5).startUrls(urls);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间："+ TimeUtil.getDateTime());
    }

//        @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 20 */1 * * ?")
    public void crawlerEastmoneyNews() throws Exception {
        String url="http://stock.eastmoney.com/a/cdpfx.html";
        Spider spider=Spider.create(new EastmoneyNewsCrawler())
                .addPipeline(resultStorage).thread(5).addUrl(url);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间："+ TimeUtil.getDateTime());
    }
    
}
