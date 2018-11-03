package com.crawler.example.util;

import com.alibaba.fastjson.JSONObject;
import com.crawler.example.bean.StockLatest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockPriceUtil {

    public static StockLatest getStockLatestPrice(String code){
        try(Jedis jedis=RedisUtil.getJedis()){
            String str=jedis.hget("stock_latest",code);
            return JSONUtil.JSON2Object(str,StockLatest.class);
        }
    }
    public static Map<String,StockLatest> getMapStockLatestPrice(List<String> codes){
        Map<String,StockLatest> mapStock=new HashMap<>();
        try(Jedis jedis=RedisUtil.getJedis()){
            Pipeline pipeline=jedis.pipelined();
            for(String code_type:codes){
                pipeline.hget("stock_latest",code_type);
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                if(null==obj) continue;
                StockLatest sl=JSONUtil.JSON2Object(obj.toString(),StockLatest.class);
                mapStock.put(sl.getCode(),sl);
            }
        }
        return mapStock;
    }
    public static Map<String,String> getMapStockLatestPrices(List<String> codes){
        Map<String,String> mapStock=new HashMap<>();
        try(Jedis jedis=RedisUtil.getJedis()){
            Pipeline pipeline=jedis.pipelined();
            for(String code_type:codes){
                pipeline.hget("stock_latest",code_type);
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                if(null==obj) continue;
                StockLatest sl=JSONUtil.JSON2Object(obj.toString(),StockLatest.class);
                mapStock.put(sl.getCode(),sl.getNow());
            }
        }
        return mapStock;
    }
}
