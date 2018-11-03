package com.crawler.example.bean;

import com.crawler.example.util.TimeUtil;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;

public class News {
    private String img_url;
    private String title;
    private String source;//来源
    private String url;
    private String content;
    private String whole_content;
    private String publish_time;
    private String insert_time = TimeUtil.getDateTime();
    private String guid;
    private String update_time = TimeUtil.getDateTime();
    private String ch_source;
    private String board;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCh_source() {
        return ch_source;
    }

    public void setCh_source(String ch_source) {
        this.ch_source = ch_source;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getWhole_content() {
        return whole_content;
    }

    public void setWhole_content(String whole_content) {
        this.whole_content = whole_content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "News{" +
                "img_url='" + img_url + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", whole_content='" + whole_content + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", insert_time='" + insert_time + '\'' +
                ", guid='" + guid + '\'' +
                ", update_time='" + update_time + '\'' +
                ", ch_source='" + ch_source + '\'' +
                ", board='" + board + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
