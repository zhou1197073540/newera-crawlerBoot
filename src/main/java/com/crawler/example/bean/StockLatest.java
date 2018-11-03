package com.crawler.example.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class StockLatest {
    private String name;
    private String open;
    private String close;
    private String now;
    private String high;
    private String low;
    private String date;
    private String time;
    private String type;
    private String status;
    private String full_code;
    private String code;
}
