package com.crawler.example.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.HangQing;
import com.crawler.example.util.StringUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class HangQingCrawler implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        JSONObject object=JSONObject.parseObject(page.getRawText());
        if(!object.containsKey("Data")) return;
        List<HangQing> hqs=new ArrayList<>();
        Object[] objs=JSONArray.parseArray(object.getString("Data")).toArray();
        for (Object obj:objs){
           JSONObject ob=JSONObject.parseObject(obj.toString());
           if(ob.containsKey("Data")){
               JSONObject oo=JSONObject.parseObject(ob.getString("Data"));
               HangQing hq=new HangQing();
               hq.setCode(oo.getString("Code"));
               hq.setFull_code(StringUtil.prefixStockCode(oo.getString("Code")));
               hq.setCode_name(oo.getString("Name"));
               hq.setDesc(oo.getString("Desc"));
               hq.setInfo(oo.getString("Info"));
               hq.setTime(oo.getString("Time"));
               hq.setDirection(oo.getString("Direction"));
               if(StringUtil.isEmpty(hq.getCode(),hq.getTime())) return;
               hqs.add(hq);
           }
        }
        page.putField("hq_res",hqs);
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        String url="http://recommend.eastmoney.com/pushdata/api/msg/get?cb=&uid=7d40a7e943b8d94f6cb4cc8dd5ad91cc&count=30&type=3";
        Spider.create(new HangQingCrawler()).thread(5).addUrl(url).run();
    }
}
