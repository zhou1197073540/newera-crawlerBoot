package com.crawler.example.mapper;

import com.crawler.example.bean.AKShares;
import com.crawler.example.bean.HangQing;
import com.crawler.example.bean.PledgeGD;
import com.crawler.example.bean.PledgeRatio;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EastmoneyMapper {

    @Select("select ticker from sw_stock_industry where \"startDate\"!=''")
    List<String> queryAllCodes();

    @Insert("<script>" +
            "insert into stock_pledge_ratio VALUES " +
            "<foreach collection='results' item='res' separator=','> " +
            "(#{res.code},#{res.tdate},#{res.zybl},#{res.zygs},#{res.zysz}," +
            "#{res.zybs},#{res.wxsgzys},#{res.xsgzys},#{res.zdf},#{res.hy}) " +
            "</foreach> " +
            "on conflict(code,tdate) do nothing" +
            "</script>")
    void saveBatchPledge(@Param("results") List<PledgeRatio> results);

    @Insert("<script>" +
            "insert into stock_pledge_gd(code,gdmc,zybs,zygs,zygfsz,cgbl,gbbl,pcx,yjx,update_date) VALUES " +
            "<foreach collection='ress' item='res' separator=','> " +
            "(#{res.code},#{res.gdmc},#{res.zybs},#{res.zygs},#{res.zygfsz}," +
            "#{res.cgbl},#{res.gbbl},#{res.pcx},#{res.yjx},#{res.update_date}) " +
            "</foreach> " +
            "on conflict(code,gdmc) do nothing" +
            "</script>")
    void saveBatchPledgeGD(@Param("ress") List<PledgeGD> ress);

    @Update("<script>" +
            "update stock_pledge_gd st set pcx=tmp.pcx,yjx=tmp.yjx,zygfsz=tmp.zygfsz from " +
            "(values " +
            "<foreach collection='list' item='item' separator=','> " +
            "   (#{item.code},#{item.gdmc},#{item.pcx},#{item.yjx},#{item.zygfsz}) " +
            "</foreach> " +
            ") as tmp(code,gdmc,pcx,yjx,zygfsz)  " +
            "where st.code=tmp.code and st.gdmc=tmp.gdmc " +
            "</script>")
    void updateBatchPledgeGD(List<PledgeGD> ress);

    @Select("select h_code k_code,a_code from ahk_price_ratio")
    List<AKShares> queryAK();

    @Select("select a_code from ahk_price_ratio where h_code=#{code} limit 1")
    String queryACodeByKCode(String code);

    @Insert("insert into ahk_realtime_price_ratio values(" +
            "#{a_code},#{k_code},#{a_name},#{k_name},#{a_latest_price}::float,#{k_latest_price}::float," +
            "#{price_ratio}::float,#{datetime},#{a_time},#{k_time},#{a_close}::float,#{k_close}::float)" +
            "on conflict(a_code,k_code,datetime) do nothing")
    void saveOneAKShare(AKShares ak_res);

    @Select("select DISTINCT(hy) from stock_pledge_ratio GROUP BY(hy)")
    List<String> queryAllHY();

    @Select("select * from stock_pledge_ratio " +
            "where hy=#{hy} and " +
            "tdate=(select max(tdate) from stock_pledge_ratio) " +
            "ORDER BY zybl::float")
    List<PledgeRatio> queryRatioByHY(String hy);

    @Update("<script>" +
            "update stock_pledge_ratio st set c1=tmp.c1 " +
            "from (values " +
            "<foreach collection='list' item='item' separator=','>" +
            "   (#{item.code},#{item.tdate},#{item.c1})" +
            "</foreach>" +
            ") as tmp(code,tdate,c1) " +
            "where st.code=tmp.code and st.tdate=tmp.tdate" +
            "</script>")
    void updateBatchRatioC1(@Param("list") List<PledgeRatio> ratios);

    @Select("select DISTINCT(code) from stock_pledge_gd")
    List<String> queryAllStockGD();

    @Select("select id,code,pcx,yjx from stock_pledge_gd where pcx!='-' and pcx!='0.0' and yjx!='-' and  yjx!='0.0'")
    List<PledgeGD> queryAllGDByCode();


    @Update("<script>" +
            "update stock_pledge_gd st set c2=tmp.c2 " +
            "from (values " +
            "<foreach collection='list' item='item' separator=','>" +
            "   (#{item.id},#{item.c2})" +
            "</foreach>" +
            ") as tmp(id,c2) " +
            "where st.id=tmp.id" +
            "</script>")
    void updateBatchPledgeGDC2(List<PledgeGD> gds);

    @Insert("<script>" +
            "insert into hang_qing(code,full_code,code_name,info,\"desc\",\"time\",direction) values " +
            "<foreach collection='list' item='item' separator=','> " +
            "(#{item.code},#{item.full_code},#{item.code_name},#{item.info},#{item.desc},#{item.time},#{item.direction}::int) " +
            "</foreach>" +
            "on conflict(code,time) do nothing " +
            "</script>")
    void saveBatchHangQing(@Param("list") List<HangQing> hq_res);
}
