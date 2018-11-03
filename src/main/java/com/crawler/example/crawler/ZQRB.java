package com.crawler.example.crawler;

import com.crawler.example.bean.News;
import com.crawler.example.util.HashUtil;
import com.crawler.example.util.StringUtil;
import com.crawler.example.util.TimeUtil;
import org.apache.bcel.generic.NEW;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ZQRB implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private static HashSet<String> urls_ZQRB=new HashSet<>();
    public static String date="2018-05-21";

    @Override
    public void process(Page page) {
        isClearUrls();
        boolean isHave=page.getUrl().regex("/\\d{4}-\\d{2}-\\d{2}/").match();
        if(!isHave){
            String target_url=page.getUrl().toString();
            List<String> lis=page.getHtml().xpath("//div[@class='listMain']//li").all();
            for(String li:lis){
                String publish_time=new Html(li).xpath("//span[@class='date']/text()").get();
                String title=new Html(li).xpath("//a/text()").get();
                String url=new Html(li).xpath("//a/@href").get();
                if(urls_ZQRB.contains(url)) continue;
                News news=new News();
                if(!StringUtil.isEmpty(title,url)){
                    news.setTitle(title);
                    news.setUrl(url);
                    news.setCh_source("证券日报");
                    news.setBoard(getBoard(target_url));
                    news.setPublish_time(publish_time);
                    news.setGuid(HashUtil.encodeByMD5(url));
                    urls_ZQRB.add(url);
                    page.addTargetRequest(new Request(url).putExtra("ZQRB_Bean",news));
                }
            }
        }else{
            News zqrb=(News)page.getRequest().getExtra("ZQRB_Bean");
            String content="";
            try{
                 content= Jsoup.clean(page.getHtml().xpath("//div[@class='content-lcq']").toString(),"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            }catch (Exception e){
                content= Jsoup.clean(page.getHtml().xpath("//div[@class='content']").toString(),"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            }
            zqrb.setContent(StringUtil.gainFirst100Words(content));
            zqrb.setWhole_content(content);
            page.putField("result",zqrb);
        }
    }

    private String getBoard(String target_url) {
        if(StringUtil.isEmpty(target_url)) return null;
        if(target_url.contains("/hongguanjingji/")){
            return "宏观经济";
        }else if(target_url.contains("/hangyedongtai/")){
            return "产业风向";
        }else if(target_url.contains("/guojijingji/")){
            return "国际要闻";
        }else {
            return "其他";
        }
    }

    private void isClearUrls(){
        String today=TimeUtil.getCurDateTime();
        if(TimeUtil.isNextDate(date,today)){
            urls_ZQRB.clear();
            date=today;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url_economy="http://www.zqrb.cn/finance/hongguanjingji/index.html";
        Spider.create(new ZQRB()).addUrl(url_economy).thread(5).run();
    }
}
