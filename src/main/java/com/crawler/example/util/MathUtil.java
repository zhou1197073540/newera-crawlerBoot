package com.crawler.example.util;

import com.crawler.example.bean.PledgeRatio;

import java.math.BigDecimal;

public class MathUtil {

    public static float calC1(int rank,int size) {
        if(size<=0) return 0;
        BigDecimal res=new BigDecimal(rank).divide(new BigDecimal(size),4,BigDecimal.ROUND_HALF_UP);
        return new BigDecimal(100).multiply(res).floatValue();
    }
}
