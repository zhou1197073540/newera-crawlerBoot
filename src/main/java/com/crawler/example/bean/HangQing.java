package com.crawler.example.bean;

import lombok.Data;

@Data
public class HangQing {
    private String code;
    private String full_code;
    private String code_name;
    private String info;//异动信息
    private String desc;//事件
    private String time;//异动时间
    private String direction;
}
