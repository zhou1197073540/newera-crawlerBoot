package com.crawler.example.service;

import com.crawler.example.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ResultService {
    @Autowired
    NewsMapper newsMapper;

    public List<String> selectWeiBoUrl() {
        String weibo_api="https://m.weibo.cn/api/container/getIndex?type=uid&value=%s";
        List<String> list=newsMapper.selectWeiBoUrl();
        List<String> urls=new ArrayList<>();
        for(String one:list){
            Pattern p=Pattern.compile("\\d{5,}");
            Matcher m=p.matcher(one);
            if(m.find()){
                String value=m.group().replace("100505","");
                urls.add(String.format(weibo_api,value));
            }
        }
        return urls;
    }
}
