package com.crawler.example.crawler;

import com.crawler.example.bean.News;
import com.crawler.example.storage.ResultStorage;
import com.crawler.example.util.HashUtil;
import com.crawler.example.util.StringUtil;
import com.crawler.example.util.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.HashSet;
import java.util.List;

/**
 * 证券时报网
 */
public class ZQSBW implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private static HashSet<String> urls_set=new HashSet<>();
    public static String date="2018-05-29";

    @Override
    public void process(Page page) {
        isClearUrls();
        String target_url=page.getUrl().toString();
        if(isCateUrl(target_url)){
            List<String> lis=page.getHtml().xpath("//ul[@class='news_list2']//li").all();
            for(String li:lis){
                String publish_date=new Html(li).xpath("//span/text()").get();
                String publish_time=new Html(li).xpath("//span/i/text()").get();
                String title=new Html(li).xpath("//a/text()").get();
                String url=new Html(li).xpath("//a/@href").get();
                if(urls_set.contains(url)) continue;
                News news=new News();
                if(!StringUtil.isEmpty(title,url)){
                    news.setTitle(title);
                    news.setUrl(url);
                    news.setCh_source("证券时报网");
                    news.setBoard(getBoard(target_url));
                    news.setPublish_time(publish_date+" "+publish_time);
                    news.setGuid(HashUtil.encodeByMD5(url));
                    urls_set.add(url);
                    page.addTargetRequest(new Request(url).putExtra("ZQSBW_Bean",news));
                }
            }
        }else{
            News zqrb=(News)page.getRequest().getExtra("ZQSBW_Bean");
            String content= Jsoup.clean(page.getHtml().xpath("//div[@class=\"txt_con\"]").toString(),"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            zqrb.setContent(StringUtil.gainFirst100Words(content));
            zqrb.setWhole_content(content);
            page.putField("result",zqrb);
        }
    }

    public boolean isCateUrl(String url){
        if(StringUtil.isEmpty(url)) return false;
        if(url.contains("/dapan/")||url.contains("/zhuli/")
                ||url.contains("/bankuai/")){
            return true;
        }
        return false;
    }
    private String getBoard(String target_url) {
        if(StringUtil.isEmpty(target_url)) return null;
        if(target_url.contains("/dapan/")){
            return "大盘";
        }else if(target_url.contains("/zhuli/")){
            return "主力资金";
        }else if(target_url.contains("/bankuai/")){
            return "板块个股";
        }else {
            return "其他";
        }
    }

    private void isClearUrls(){
        String today=TimeUtil.getCurDateTime();
        if(TimeUtil.isNextDate(date,today)){
            urls_set.clear();
            date=today;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        String url_economy="http://stock.stcn.com/dapan/index.shtml";
        Spider.create(new ZQSBW()).addUrl(url_economy).thread(5).run();
    }
}
