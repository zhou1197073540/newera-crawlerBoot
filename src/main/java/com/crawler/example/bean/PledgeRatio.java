package com.crawler.example.bean;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
public class PledgeRatio {
    private String code;
    private String tdate;
    private String zybl;
    private String zygs;
    private String zysz;
    private String zybs;
    private String wxsgzys;
    private String xsgzys;
    private String zdf;
    private String hy;
    private float c1;
    private int hy_rank;//行业排名
}
