package com.fh.controller;

import com.alibaba.fastjson.JSONArray;
import com.fh.model.Area;
import com.fh.util.JsonData;
import com.fh.service.AreaService;
import com.fh.util.RedisUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.Jedis;


@Controller
@RequestMapping("AreaController")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping("selectArea")
    @ResponseBody
    public JsonData selectArea(){
        Jedis jedis=RedisUtl.getJedis();
        Map<String, String> map = jedis.hgetAll("area_wr");
        Set<String> keys = map.keySet();
        //查询出来的会员信息
        List<Area> areaList = new ArrayList<>();
        for (String key : keys) {
            Area area = JSONArray.parseObject(map.get(key), Area.class);
            areaList.add(area);
        }
        RedisUtl.returnJedis(jedis);
        return JsonData.getJsonSuccess(areaList);
    }
}
