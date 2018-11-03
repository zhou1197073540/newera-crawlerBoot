package com.crawler.example.storage;

import com.crawler.example.bean.News;
import com.crawler.example.mapper.NewsMapper;
import com.crawler.example.service.ResultService;
import com.crawler.example.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 微博结果存储
 */
@Component("listResultStorage")
public class ListResultStorage implements Pipeline {
    @Autowired
    NewsMapper newsMapper;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String source = task.getSite().getDomain();
        List<News> result = resultItems.get("result");
        if (result == null) return;
        for (News one : result) {
            try {
                one.setSource(source);
                one.setContent(StringUtil.formatContentTxt(one.getContent()));
                one.setWhole_content(StringUtil.formatContentTxt(one.getWhole_content()));
                saveOrUpdate(one);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int saveOrUpdate(News res) {
        if (res == null || StringUtil.isEmpty(res.getGuid())) return -1;
        if (StringUtil.isEmpty(res.getImg_url())) {
            String url_ = "http://image.mzkj88.com/head/";
            String img = newsMapper.selectRandomImage("head");
            res.setImg_url(url_ + img);
        }
        if (newsMapper.insertWeiBo(res) > 0) {
            return newsMapper.insertNewsContent(res);
        }
        return 0;
    }

}
