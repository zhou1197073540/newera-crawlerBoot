package com.crawler.example.crawler;

import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.News;
import com.crawler.example.util.HashUtil;
import com.crawler.example.util.StringUtil;
import com.crawler.example.util.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.jsoup.nodes.Document.OutputSettings;

public class SinaWeiBo implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000);
    private String weibo_url="https://m.weibo.cn/status/%s";

    @Override
    public void process(Page page){
        String target_url=page.getUrl().toString();
        if(target_url.contains("type=uid&")&&!target_url.contains("containerid")){
            parserTitlePage(page);
        }else if(target_url.contains("type=uid&")&&target_url.contains("containerid")){
            List<News> weibos=new ArrayList<>();
            List<String> lists = page.getJson().jsonPath("$.data.cards").all();
            for(String one:lists){
                JSONObject obj=JSONObject.parseObject(one);
                if(obj.containsKey("mblog")){
                    JSONObject ob=JSONObject.parseObject(obj.getString("mblog"));
                    String id=ob.getString("id");
                    String create_at=ob.getString("created_at");
                    String text=ob.getString("text");
                    String content= Jsoup.clean(text,"",Whitelist.none(), new OutputSettings().prettyPrint(false));
                    if(!StringUtil.isEmpty(content)){
                        News weibo=new News();
                        weibo.setPublish_time(StringUtil.getRealTime(create_at));
                        weibo.setCh_source("新浪微博");
                        weibo.setUrl(String.format(weibo_url,id));
                        weibo.setType("weibo");
                        weibo.setBoard("游客_"+HashUtil.generateRandomNum(8));
                        if(content.contains("...全文")){
                            page.addTargetRequest(new Request(String.format(weibo_url,id)).putExtra("weibo_bean",weibo));
                        }else{
                            weibo.setGuid(HashUtil.encodeByMD5(content));
                            weibo.setContent(StringUtil.gainFirst100Words(content));
                            weibo.setWhole_content(content);
                            weibos.add(weibo);
                        }
                    }
                }
            }
            page.putField("result",weibos);
        }else if(target_url.contains("id=")){
            List<News> weibos=new ArrayList<>();
            News weibo=(News)page.getRequest().getExtra("weibo_bean");
            String longTextContent = page.getJson().jsonPath("$.data.longTextContent").get();
            String content= Jsoup.clean(longTextContent,"",Whitelist.none(), new OutputSettings().prettyPrint(false));
            if(!StringUtil.isEmpty(content)){
                weibo.setGuid(HashUtil.encodeByMD5(content));
                weibo.setContent(StringUtil.gainFirst100Words(content));
                weibo.setWhole_content(content);
                weibos.add(weibo);
            }
            page.putField("result",weibos);
        }
    }

    public static String getRandomTeacher(){
        String[] teachers={"陈老师","周老师","胡老师","李老师","吴老师","杨老师"};
        int index = (int) (Math.random() * teachers.length);
        return teachers[index];
    }

    @Override
    public Site getSite() {
        return site;
    }

    private void parserTitlePage(Page page) {
        String target_url=page.getUrl().toString();
        List<String> lists = page.getJson().jsonPath("$.data.tabsInfo.tabs").all();
        for (String list : lists) {
            JSONObject json=JSONObject.parseObject(list);
            String title = json.getString("title");
            if("微博".equals(title)){
                String containerid=json.getString("containerid");
                if(!StringUtil.isEmpty(containerid)){
                    News sina = new News();
                    sina.setTitle(title);
                    sina.setCh_source("新浪微博");
                    String child_urls=target_url+"&containerid=%s";
                    page.addTargetRequest(new Request(String.format(child_urls,containerid)).putExtra("sina_weibo",sina));
                }
               break;
            }
        }
    }

    public static void main(String[] args) {
//        String url_economy="https://m.weibo.cn/api/container/getIndex?type=uid&value=2072724293";
//        Spider.create(new SinaWeiBo()).addUrl(url_economy).thread(1).run();
        String txt="【证券日报：A股做好三件事就可以走强】证券日报刊文称，对中国股市来说，需要做好三件事，第一，准确说明贸易战可能带来的影响，减少投资市场的盲从盲动；第二，及时化解金融政策和资本市场政策方面的疑虑和疑点，近期针对整体流动性和股票质押问题，已经释放了明确政策，推出了有效措施；第三，要发挥好已有的机构投资者的作用，沧海横流方显英雄本色，境内机构投资者应该按照价值投资理念，做价值投资典范。 <a data-url=\"http://t.cn/RdvapyD\" href=\"https://www.aigupiao.com/news/detail.php?id=94595\" data-hide=\"\"><span class='url-icon'><img style='width: 1rem;height: 1rem' src='https://h5.sinaimg.cn/upload/2015/09/25/3/timeline_card_small_web_default.png'></span><span class=\"surl-text\">网页链接</span></a>";
        String content= Jsoup.clean(txt,"",Whitelist.none(), new OutputSettings().prettyPrint(false));
        System.out.println(content);
    }
}
