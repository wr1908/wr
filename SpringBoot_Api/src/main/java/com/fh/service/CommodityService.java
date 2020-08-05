package com.fh.service;


import com.fh.model.Commodity;
import com.fh.model.CommodityInfo;

import java.util.List;

public interface CommodityService {


    List<Commodity> selectCommodity();

    List<CommodityInfo> selectSell();

    List<CommodityInfo> selectTypeById(Integer typeId);

    Commodity selectById(Integer id);
}
