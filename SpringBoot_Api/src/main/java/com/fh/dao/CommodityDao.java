package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Commodity;
import com.fh.model.CommodityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityDao extends BaseMapper<Commodity> {
    List<Commodity> selectC();

    List<CommodityInfo> selectSell();

    List<CommodityInfo> selectTypeById(Integer typeId);

    int updateCount(@Param("id") Integer id, @Param("count") Integer count);
}
