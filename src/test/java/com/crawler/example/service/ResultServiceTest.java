package com.crawler.example.service;

import com.crawler.example.bean.News;
import com.crawler.example.mapper.NewsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ResultServiceTest {
    @Autowired
    ResultService resultService;

    @Test
    public void Test(){
        resultService.selectWeiBoUrl();
    }
}