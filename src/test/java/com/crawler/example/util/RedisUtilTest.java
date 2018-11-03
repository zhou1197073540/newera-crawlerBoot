package com.crawler.example.util;

import com.crawler.example.bean.PledgeGD;
import com.crawler.example.bean.StockLatest;
import com.crawler.example.mapper.EastmoneyMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisUtilTest {
    @Autowired
    EastmoneyMapper eastmoneyMapper;

    @Test
    public void test(){
        List<PledgeGD> gds= eastmoneyMapper.queryAllGDByCode();
        if(gds==null||gds.isEmpty()) return;
        List<String> codes=new ArrayList<>();
        for(PledgeGD gd:gds){
            codes.add(StringUtil.prefixStockCode(gd.getCode()));
        }
        //获取所有股票最新价格
        Map<String, String> mapPrice = StockPriceUtil.getMapStockLatestPrices(codes);
        List<PledgeGD> gds1=new ArrayList<>();
        for(PledgeGD gd:gds){
            //当前价格
            String curPrice=mapPrice.get(gd.getCode());
            if(StringUtil.isEmpty(curPrice)) continue;
            int pcx_rate=getC2Rate(curPrice,gd.getPcx());
            int yjx_rate=getC2Rate(curPrice,gd.getYjx());
            gd.setC2(pcx_rate*100+yjx_rate*50);
            gds1.add(gd);
        }
        if(!gds1.isEmpty())eastmoneyMapper.updateBatchPledgeGDC2(gds1);
        System.out.println("计算完毕");
    }

    private int getC2Rate(String curPrice, String pcx) {
        try {
            if(Float.parseFloat(curPrice)<Float.parseFloat(pcx)) return 1;
            else return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}