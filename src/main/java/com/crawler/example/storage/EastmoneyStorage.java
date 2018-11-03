package com.crawler.example.storage;

import com.crawler.example.bean.*;
import com.crawler.example.service.EastmoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 东方财富网
 */

@Component("eastmoneyStorage")
public class EastmoneyStorage implements Pipeline {
    @Autowired
    EastmoneyService eastmoneyService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //质押股数结果
        List<PledgeRatio> zybl_res = resultItems.get("zybl_res");
        if(zybl_res!=null&&!zybl_res.isEmpty()){
            eastmoneyService.saveBatchPledge(zybl_res);
        }
        //股东结果
        List<PledgeGD> zygd_res = resultItems.get("zygd_res");
        if(zygd_res!=null&&!zygd_res.isEmpty()){
            eastmoneyService.saveBatchPledgeGD(zygd_res);
        }
        //A股港股比价结果
        AKShares ak_res = resultItems.get("AK_res");
        if(ak_res!=null){
            eastmoneyService.saveOneAKShare(ak_res);
        }
        //行情结果
        List<HangQing> hq_res = resultItems.get("hq_res");
        if(hq_res!=null&&!hq_res.isEmpty()){
            eastmoneyService.saveBatchHangQing(hq_res);
        }
    }
}
