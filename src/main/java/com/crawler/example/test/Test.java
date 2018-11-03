package com.crawler.example.test;

import com.crawler.example.util.SampleHttpUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashSet;

public class Test implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    @Override
    public void process(Page page) {
        String content=page.getHtml().toString();
        System.out.println("==================="+content);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        String url="https://www.weibo.com/u/1537790411";
//        Spider.create(new Test()).addUrl(url).thread(5).runAsync();
        String url="https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https://weibo.com/u/3277358335?is_all=1&domain=.weibo.com&sudaref=https://passport.weibo.com/visitor/visitor?entry=miniblog&a=enter&url=https%3A%2F%2Fweibo.com%2Fu%2F3277358335%3Fis_all%3D1&domain=.weibo.com&ua=php-sso_sdk_client-0.6.28&_rand=1528428898.9731&ua=php-sso_sdk_client-0.6.28&_rand=1528428942.4391";
        String html="";
        try {
            html= SampleHttpUtil.getResult(url,"gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(html);
    }
}
