package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.model.Commodity;
import com.fh.model.CommodityInfo;
import com.fh.service.CommodityService;
import com.fh.util.JsonData;
import com.fh.util.RedisUse;
import com.fh.util.RedisUtl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.List;

@RequestMapping("CommodityController")
@Controller
public class CommodityController {

    @Autowired
    private CommodityService commodityService;


    @RequestMapping("selectSell")
    @ResponseBody
    public JsonData selectSell(){
        Jedis jedis = RedisUtl.getJedis();
        String commodityHot_wr = jedis.get("commodityHot_wr");
        if(StringUtils.isEmpty(commodityHot_wr)){
            List<CommodityInfo> commodityInfos=commodityService.selectSell();
            commodityHot_wr = JSONObject.toJSONString(commodityInfos);
            RedisUse.set("commodityHot_wr",commodityHot_wr);
        }
        return JsonData.getJsonSuccess(commodityHot_wr);
    }


    @RequestMapping("selectCommodity")
    @ResponseBody
    public List<Commodity> selectCommodity(){
        return commodityService.selectCommodity();
    }

    @RequestMapping("selectTypeById")
    @ResponseBody
    public JsonData selectTypeById(Integer typeId){
        List<CommodityInfo> commodityInfos=commodityService.selectTypeById(typeId);
        return JsonData.getJsonSuccess(commodityInfos);
    }


    @RequestMapping("selectCommodityById")
    @ResponseBody
    public JsonData selectCommodityById(Integer id){
        Commodity commodity=commodityService.selectById(id);
        return JsonData.getJsonSuccess(commodity);
    }
}
