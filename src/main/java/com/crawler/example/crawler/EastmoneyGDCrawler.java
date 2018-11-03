package com.crawler.example.crawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.PledgeGD;
import com.crawler.example.bean.PledgeRatio;
import com.crawler.example.util.StringUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class EastmoneyGDCrawler implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        String html=page.getRawText();
//        System.out.println("============"+html+"===============");
        List<PledgeGD> list=new ArrayList<>();
        Object[] arrays= JSONArray.parseArray(html).toArray();
        for(Object obj:arrays){
            JSONObject ob=JSONObject.parseObject(obj.toString());
            PledgeGD gd=new PledgeGD();
            gd.setCode(ob.getString("scode"));
            gd.setGdmc(ob.getString("gdmc"));
            gd.setZybs(ob.getString("gd_count"));
            gd.setZygs(ob.getString("amtsharefrozen"));
            gd.setZygfsz(ob.getString("syscsz"));
            gd.setCgbl(ob.getString("syscbl"));
            gd.setGbbl(ob.getString("amtfrozenratio"));
            gd.setPcx(ob.getString("pcx_row"));
            gd.setYjx(ob.getString("yjx_row"));
            gd.setUpdate_date(fromateTDate(ob.getString("upd")));
            if(!StringUtil.isEmpty(gd.getCode(),gd.getUpdate_date())){
                list.add(gd);
            }
        }
        page.putField("zygd_res",list);
    }

    private String fromateTDate(String tdate){
        if(!StringUtil.isEmpty(tdate)&&tdate.length()>10){
            return tdate.substring(0,10);
        }
        return null;
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        String url_economy="http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=ZD_QL_LB&token=70f12f2f4f091e459a279469fe49eca5&st=tdate&sr=-1&p=1&ps=10&filter=(scode='000001')&rt=";
        String url_gd="http://dcfm.eastmoney.com/EM_MutiSvcExpandInterface/api/js/get?type=GD_SUM&token=70f12f2f4f091e459a279469fe49eca5&cmd=&st=upd&sr=-1&p=1&ps=50&filter=(scode='601360')&rt=";
        Spider.create(new EastmoneyGDCrawler()).addUrl(url_gd).thread(5).run();
    }
}
