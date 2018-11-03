package com.crawler.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsMapper<T> {

    @Transactional
    @Insert("insert into news_crawler(guid,title,url,img_url,publish_time,content,insert_time,source,ch_source,board) " +
            "values(#{guid},#{title},#{url},#{img_url},#{publish_time},#{content},#{insert_time},#{source},#{ch_source},#{board}) " +
            "ON conflict (guid) do nothing")
    Integer insertNews(T t);

    @Transactional
    @Insert("insert into news_crawler(guid,title,url,img_url,publish_time,content,insert_time,source,ch_source,board,type) " +
            "values(#{guid},#{title},#{url},#{img_url},#{publish_time},#{content},#{insert_time},#{source},#{ch_source},#{board},#{type}) " +
            "ON conflict (guid) do nothing")
    Integer insertWeiBo(T t);

    @Transactional
    @Select("select count(*) from news_crawler where guid=#{guid} and type=#{type}")
    Integer isExistByGuid(T t);

    @Transactional
    @Update("update news_crawler set content=#{content},update_time=#{update_time} " +
            "where guid=#{guid}")
    Integer updateNews(T t);

    @Transactional
    @Insert("insert into news_crawler_content(guid,content) values(#{guid},#{whole_content}) " +
            "ON conflict (guid) do nothing")
    Integer insertNewsContent(T t);

    @Transactional
    @Select("select img_name from news_images where \"type\"=#{type} ORDER BY random() limit 1")
    String selectRandomImage(@Param("type") String type);

    @Transactional
    @Select("select url from crawler_urls")
    List<String> selectWeiBoUrl();
}
