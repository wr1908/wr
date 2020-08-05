package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Vip;

import java.util.List;

public interface VipDao extends BaseMapper<Vip> {

    List<String> selectNameById(String areaId);
}
