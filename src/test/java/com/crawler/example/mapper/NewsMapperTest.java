package com.crawler.example.mapper;

import com.crawler.example.bean.News;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class NewsMapperTest {
    @Autowired
    NewsMapper newsMapper;
    @Test
    public void Test(){
        News res=new News();
        res.setType("weibo");
        res.setGuid("aec41eee200bc26ad0c27c89196cb8a2");
        int num = newsMapper.isExistByGuid(res);
        if (num <= 0) {
            newsMapper.insertWeiBo(res);
            newsMapper.insertNewsContent(res);
        }
    }
}