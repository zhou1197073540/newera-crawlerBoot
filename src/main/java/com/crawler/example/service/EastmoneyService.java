package com.crawler.example.service;

import com.crawler.example.bean.*;
import com.crawler.example.mapper.EastmoneyMapper;
import com.crawler.example.util.MathUtil;
import com.crawler.example.util.StockPriceUtil;
import com.crawler.example.util.StringUtil;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EastmoneyService {
    @Autowired
    EastmoneyMapper eastmoneyMapper;


    public List<String> queryAllCodes() {
        return eastmoneyMapper.queryAllCodes();
    }

    public void saveBatchPledge(List<PledgeRatio> results) {
        eastmoneyMapper.saveBatchPledge(results);
    }

    public void saveBatchPledgeGD(List<PledgeGD> ress) {
        //批量插入
        eastmoneyMapper.saveBatchPledgeGD(ress);

        List<PledgeGD> collects =new ArrayList<>();
        for(PledgeGD gd:ress){
            if (filterInvalid(gd)) collects.add(gd);
        }
        //批量更新
        if(!collects.isEmpty()) eastmoneyMapper.updateBatchPledgeGD(collects);
    }

    private boolean filterInvalid(PledgeGD gd) {
        if(gd.getPcx().equals("-")&& gd.getYjx().equals("-")) return false;
        if(Float.parseFloat(gd.getPcx())<=0&& Float.parseFloat(gd.getYjx())<=0) return false;
        return true;
    }


    public List<AKShares> queryAK() {
        return eastmoneyMapper.queryAK();
    }

    public String queryACodeByKCode(String code) {
        return eastmoneyMapper.queryACodeByKCode(code);
    }

    public void saveOneAKShare(AKShares ak_res) {
        eastmoneyMapper.saveOneAKShare(ak_res);
    }

    public List<String> queryAllHY() {
        return eastmoneyMapper.queryAllHY();
    }

    public List<PledgeRatio> queryRatioByHY(String hy) {
        List<PledgeRatio> ratios=eastmoneyMapper.queryRatioByHY(hy);
        if(ratios==null||ratios.isEmpty()) return null;
        int rank=1;
        for(PledgeRatio ratio:ratios){
            ratio.setHy_rank(rank);
            float c1=MathUtil.calC1(ratio.getHy_rank(),ratios.size());
            ratio.setC1(c1);
            rank++;
        }
        return ratios;
    }

    public void updateBatchRatio(List<PledgeRatio> ratios) {
        eastmoneyMapper.updateBatchRatioC1(ratios);
    }

    //查找所有含有股东的股票
    public List<String> queryAllStockGD() {
        return eastmoneyMapper.queryAllStockGD();
    }

    public void queryAVGGDByCode() {
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

    public void saveBatchHangQing(List<HangQing> hq_res) {
        eastmoneyMapper.saveBatchHangQing(hq_res);
    }
}
