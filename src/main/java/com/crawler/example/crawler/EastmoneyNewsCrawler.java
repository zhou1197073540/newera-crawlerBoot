package com.crawler.example.crawler;

import com.crawler.example.bean.News;
import com.crawler.example.util.HashUtil;
import com.crawler.example.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class EastmoneyNewsCrawler implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private static HashSet<String> urls_DFCF = new HashSet<>();

    @Override
    public void process(Page page) {
        String page_url = page.getUrl().toString();
        if (page_url.contains("/a/cdpfx")) {
            List<String> lis = page.getHtml().xpath("//div[@class='repeatList']//li").all();
            if (lis == null || lis.isEmpty()) return;
            for (String li : lis) {
                News news = new News();
                Html html = new Html(li);
                String url = html.xpath("//p[@class='title']/a/@href").toString();
                String title = html.xpath("//p[@class='title']/a/text()").toString();
                String img = html.xpath("//div[@class='image']//img/@src").toString();
                String info = html.xpath("//p[@class='info']/text()").toString();
                String publish_time = html.xpath("//p[@class='time']/text()").toString();
                if (urls_DFCF.contains(url)) continue;
                if (StringUtil.isEmpty(title, url)) continue;
                news.setTitle(title);
                news.setUrl(url);
                news.setCh_source("东方财富");
                news.setBoard("大盘分析");
                news.setImg_url(img);
                news.setContent(info);
                news.setPublish_time(formatPublisTime(publish_time));
                news.setGuid(HashUtil.encodeByMD5(url));
                urls_DFCF.add(url);
                page.addTargetRequest(new Request(url).putExtra("DFCF_Bean", news));
            }
        }else{
            News dfcf=(News)page.getRequest().getExtra("DFCF_Bean");
            String content="";
            try{
                content=Jsoup.clean(page.getHtml().xpath("//div[@id='ContentBody']").toString(),"", Whitelist.none(),new Document.OutputSettings().prettyPrint(false));
            }catch (Exception e){
                e.printStackTrace();
            }
//            System.out.println(content);
            String con=dfcf.getContent();
            dfcf.setContent(StringUtil.isEmpty(con)?StringUtil.gainFirst100Words(content):con);
            dfcf.setWhole_content(content);
            page.putField("result",dfcf);
        }
    }

    private String formatPublisTime(String time) {
        if (StringUtil.isEmpty()) return null;
        LocalDate date = LocalDate.now();
        time = time.replace("月", "-");
        String publish_time = time.replaceAll("[^0-9 -:]", "");
        return date.getYear() + "-" + publish_time.trim();
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        String url = "http://stock.eastmoney.com/a/cdpfx.html";
        Spider.create(new EastmoneyNewsCrawler()).thread(1).addUrl(url).run();
    }
}
