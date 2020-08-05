package com.fh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.dao.CommodityDao;
import com.fh.model.Area;
import com.fh.model.Commodity;
import com.fh.model.CommodityInfo;
import com.fh.service.CommodityService;
import com.fh.util.RedisUse;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {

    @Resource
    private CommodityDao commodityDao;

    @Override
    public List<Commodity> selectCommodity() {
        return commodityDao.selectC();
    }

    @Override
    public List<CommodityInfo> selectSell() {
        return commodityDao.selectSell();
    }

    @Override
    public List<CommodityInfo> selectTypeById(Integer typeId) {
        return commodityDao.selectTypeById(typeId);
    }

    @Override
    public Commodity selectById(Integer id) {
        Commodity commodity=commodityDao.selectById(id);
        String[] area=commodity.getArea().split(",");
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i <area.length ; i++) {
            String areaJson = RedisUse.hget("area_wr", area[i]);
            Area area1 = JSONObject.parseObject(areaJson, Area.class);
            sb.append(area1.getAreaName()).append(",");
        }
        commodity.setArea(sb.toString());
        return commodity;
    }


}
