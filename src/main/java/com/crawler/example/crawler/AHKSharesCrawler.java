package com.crawler.example.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.AKShares;
import com.crawler.example.bean.News;
import com.crawler.example.bean.PledgeRatio;
import com.crawler.example.service.EastmoneyService;
import com.crawler.example.util.StringUtil;
import com.crawler.example.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AHKSharesCrawler implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private EastmoneyService eastmoneyService;
    private AHKSharesCrawler() {}
    public AHKSharesCrawler(EastmoneyService eastmoneyService) {
        this.eastmoneyService = eastmoneyService;
    }

    @Override
    public void process(Page page) {
        String url=page.getUrl().toString();
        String code=url.substring(url.indexOf("=")+1,url.length());
        String html=fromateContent(page.getRawText());
        String[] strs=html.split(",");
        if (StringUtil.isEmpty(html,code)) return;
        if(strs.length<=0) return;
        if(code.contains("hk")){
            String a_code=eastmoneyService.queryACodeByKCode(code);
            AKShares hk=new AKShares();
            hk.setDatetime(TimeUtil.getHHmm());
            hk.setK_code(code);
            hk.setK_name(strs[1]);
            hk.setK_latest_price(strs[6]);
            hk.setK_close(strs[3]);
            hk.setK_time(strs[17].replace("/","-")+" "+strs[18]);
            String url_ = "http://hq.sinajs.cn/list=%s";
            page.addTargetRequest(new Request(String.format(url_,a_code)).putExtra("hk",hk));
        }else{
            AKShares ak= (AKShares) page.getRequest().getExtra("hk");
            if(ak==null) return;
            ak.setA_code(code);
            ak.setA_name(strs[0]);
            ak.setA_latest_price(strs[3]);
            ak.setA_close(strs[2]);
            ak.setA_time(strs[30]+" "+strs[31]);
            ak.setPrice_ratio(calRatio(ak.getA_latest_price(),ak.getK_latest_price()));
            if(!StringUtil.isEmpty(ak.getA_code(),ak.getK_code(),ak.getDatetime())){
                page.putField("AK_res",ak);
            }
        }
    }

    private String calRatio(String a_latest_price, String k_latest_price) {
        if(Float.parseFloat(k_latest_price)<=0) return null;
        BigDecimal transferRMB=new BigDecimal(k_latest_price).multiply(new BigDecimal("0.89"));
        return new BigDecimal(a_latest_price).divide(transferRMB,4,BigDecimal.ROUND_HALF_UP).toString();
    }

    private String fromateContent(String html){
        String content=null;
        if(html.contains("=\"")){
            content=html.substring(html.indexOf("=\"")+2,html.length());
            content=content.substring(0,content.indexOf("\";"));
        }
        return content;
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        String url_economy="http://hq.sinajs.cn/list=sz000513";
        Spider.create(new AHKSharesCrawler()).addUrl(url_economy).thread(5).run();
    }
}
