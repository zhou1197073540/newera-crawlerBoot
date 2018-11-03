package com.crawler.example.crawler;

import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.News;
import com.crawler.example.util.HashUtil;
import com.crawler.example.util.StringUtil;
import com.crawler.example.util.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.processor.PageProcessor;


import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.jsoup.nodes.Document.*;

public class SinaA implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private static HashSet<String> urls_sina=new HashSet<>();
    public static String date="2018-05-21";

    @Override
    public void process(Page page) {
        String today=TimeUtil.getCurDateTime();
        if(TimeUtil.isNextDate(date,today)){
            urls_sina.clear();
            date=today;
        }
        if(page.getUrl().toString().contains("sort_id")){
            parserTitlePage(page);
        }else{
            News sian=(News)page.getRequest().getExtra("sina_title");
            String content= Jsoup.clean(page.getHtml().xpath("//div[@id='artibody']").toString(),"",Whitelist.none(), new OutputSettings().prettyPrint(false));
            sian.setContent(StringUtil.gainFirst100Words(content));
            sian.setWhole_content(content);
            page.putField("result",sian);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private void parserTitlePage(Page page) {
        String target_url=page.getUrl().toString();
        List<String> lists = page.getJson().jsonPath("$.result.data.lists").all();
        for (String list : lists) {
            JSONObject json=JSONObject.parseObject(list);
            String title = json.getString("title");
            String images = StringUtil.removeMark(json.getString("image"));
            String publish_time = json.getString("create_time");
            String source = json.getString("username");
            String news_url=json.getString("url");
            if(urls_sina.contains(news_url)) continue;
            if (!StringUtil.isEmpty(title,news_url)) {
                News sina = new News();
                sina.setTitle(title);
                if(!images.startsWith("https:")) images="https:"+images;
                sina.setImg_url(images);
                sina.setSource(source);
                sina.setPublish_time(TimeUtil.getTime(Integer.parseInt(publish_time)));
                if(!news_url.startsWith("https:")) news_url="https:"+news_url;
                sina.setUrl(news_url);
                sina.setCh_source("新浪财经综合");
                sina.setBoard(getBoard(target_url));
                sina.setGuid(HashUtil.encodeByMD5(news_url));
                urls_sina.add(news_url);
                page.addTargetRequest(new Request(news_url).putExtra("sina_title",sina));
            }
        }
    }

    private String getBoard(String target_url) {
        if(StringUtil.isEmpty(target_url)) return null;
        if(target_url.contains("sort_id=2")){
            return "宏观经济";
        }else if(target_url.contains("sort_id=3")){
            return "A股";
        }else {
            return "其他";
        }
    }

    public static void main(String[] args) {
        String url_economy="http://cj.sina.com.cn/k/api/article/lists_by_column?sort_id=2&page=1";
        Spider.create(new SinaA()).addUrl(url_economy).thread(5).run();
    }
}
