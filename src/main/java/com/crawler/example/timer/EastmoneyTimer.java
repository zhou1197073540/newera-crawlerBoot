package com.crawler.example.timer;

import com.crawler.example.bean.AKShares;
import com.crawler.example.bean.PledgeGD;
import com.crawler.example.bean.PledgeRatio;
import com.crawler.example.crawler.*;
import com.crawler.example.service.EastmoneyService;
import com.crawler.example.storage.EastmoneyStorage;
import com.crawler.example.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A股港股比价
 */
@Component
public class EastmoneyTimer {
    @Autowired
    EastmoneyStorage eastmoneyStorage;
    @Autowired
    EastmoneyService eastmoneyService;

    //    @Scheduled(cron = "0 33 16 * * ?")
    @Scheduled(cron = "0/20 0 2 * * SUN")
    public void crawlerYZBL() throws Exception {
//        //质押比例
        String url_zy = "http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=ZD_QL_LB&token=70f12f2f4f091e459a279469fe49eca5&st=tdate&sr=-1&p=1&ps=10&filter=(scode='%s')&rt=";
        List<String> urls = new ArrayList<>();
        List<String> codes = eastmoneyService.queryAllCodes();
        for (String str : codes) {
            urls.add(String.format(url_zy, str));
        }
//        urls.add(String.format(url_zy,"000001"));
        Spider spider = Spider.create(new EastmoneyCrawler()).startUrls(urls)
                .addPipeline(eastmoneyStorage).thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间：" + TimeUtil.getDateTime());
    }

//    @Scheduled(cron = "0 09 17 * * ?")
    @Scheduled(cron = "0/20 0 4 * * SUN")
    public void crawlerGD() throws Exception {
//        //质押股东
        String url_zy = "http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=GD_SUM&token=70f12f2f4f091e459a279469fe49eca5&cmd=&st=upd&sr=-1&p=1&ps=50&filter=(scode='%s')&rt=";
        List<String> urls = new ArrayList<>();
        List<String> codes = eastmoneyService.queryAllCodes();
        for (String str : codes) {
            urls.add(String.format(url_zy, str));
        }
//        urls.add(String.format(url_zy,"601360"));
        Spider spider = Spider.create(new EastmoneyGDCrawler()).startUrls(urls)
                .addPipeline(eastmoneyStorage).thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间：" + TimeUtil.getDateTime());
    }

    /**
     * 计算行业危险指数
     *
     * @throws Exception
     */
//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0/40 0 18 * * SUN")
    public void calC1RiskIndex() throws Exception {
        List<String> hys = eastmoneyService.queryAllHY();
        for (String hy : hys) {
            List<PledgeRatio> ratios = eastmoneyService.queryRatioByHY(hy);
            if (ratios != null) {
                eastmoneyService.updateBatchRatio(ratios);
                System.out.println("行业危险系数计算完成：" + hy);
            }
        }
    }

    /**
     * 计算股票股东危险指数
     */
//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0/40 5 18 * * SUN")
    public void calC2RiskIndex() throws Exception {
        eastmoneyService.queryAVGGDByCode();
    }

    /**
     * A股港股价格
     */
//    @Scheduled(cron = "0 17 17 * * ?")
    @Scheduled(cron = "0 0/5 9-15 * * ?")
    public void crawlerAK() throws Exception {
        LocalTime time = LocalDateTime.now().toLocalTime();
        if ((time.isAfter(LocalTime.of(9, 29)) && time.isBefore(LocalTime.of(11, 31))) ||
                (time.isAfter(LocalTime.of(12, 59)) && time.isBefore(LocalTime.of(15, 1)))) {
            List<String> urls = new ArrayList<>();
            String url = "http://hq.sinajs.cn/list=%s";
            List<AKShares> aks = eastmoneyService.queryAK();
            for (AKShares ak : aks) {
                urls.add(String.format(url, ak.getK_code()));
            }
//            urls.add(String.format(url,"hk01108"));
            Spider spider = Spider.create(new AHKSharesCrawler(eastmoneyService)).startUrls(urls)
                    .addPipeline(eastmoneyStorage).thread(5);
            spider.setExitWhenComplete(true);
            spider.start();
            spider.stop();
        }
        System.out.println("本次结束时间：" + TimeUtil.getDateTime());
    }

    /**
     * http://stock.eastmoney.com/a/cdpfx.html
     * 东方财富-个性推荐-行情
     */
//    @Scheduled(cron = "0 */1 14 * * MON-FRI")
    @Scheduled(cron = "0 0/1 9-15 * * MON-FRI")
    public void crawlerHangQing() throws Exception {
        String url = "http://recommend.eastmoney.com/pushdata/api/msg/get?cb=&uid=7d40a7e943b8d94f6cb4cc8dd5ad91cc&count=30&type=3";
        Spider spider = Spider.create(new HangQingCrawler()).addUrl(url)
                .addPipeline(eastmoneyStorage);
        spider.setExitWhenComplete(true);
        spider.start();
        spider.stop();
        System.out.println("本次结束时间：" + TimeUtil.getDateTime());
    }

}
