package com.crawler.example.storage;

import com.crawler.example.bean.News;
import com.crawler.example.mapper.NewsMapper;
import com.crawler.example.util.KafkaUtil;
import com.crawler.example.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 新闻结果存储
 */
@Component("resultStorage")
public class ResultStorage implements Pipeline {
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    KafkaUtil kafkaUtil;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String source = task.getSite().getDomain();
        News result = resultItems.get("result");
        if (result == null) return;
        try {
            result.setSource(source);
            result.setContent(StringUtil.formatContentTxt(result.getContent()));
            result.setWhole_content(StringUtil.formatContentTxt(result.getWhole_content()));
            saveOrUpdate(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Value("${input-topic}")
    private String topic;
    @Autowired
    ObjectMapper jacksonMapper;
    private final Logger logger = LoggerFactory.getLogger(ResultStorage.class);

    private void sendMessage(News news) {
        final ObjectWriter objectWriter = jacksonMapper.writerFor(News.class);
        try {
            String sendStr = objectWriter.writeValueAsString(news);
            kafkaUtil.sendMessage(topic, sendStr);
        } catch (JsonProcessingException e) {
            logger.error("can not send message {} ", news, e);
        }

    }

    private void saveOrUpdate(News res) throws JsonProcessingException {
        if (res == null || StringUtil.isEmpty(res.getGuid(),res.getWhole_content())) return ;
        if (StringUtil.isEmpty(res.getImg_url())) {
            String url_ = "http://image.mzkj88.com/news/";
            String img = newsMapper.selectRandomImage("news");
            res.setImg_url(url_ + img);
        }
        if (newsMapper.insertNews(res) > 0) {
            int num=newsMapper.insertNewsContent(res);
//            sendMessage(res);
        }
//        int num=newsMapper.insertNewsContent(res);
//        System.out.println("content:"+num+"插入新纪录======"+res.getTitle());
//        return num;
    }

}
