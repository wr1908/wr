package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.OrderProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderProductDao extends BaseMapper<OrderProductDao> {


    void bathAdd(@Param("list") List<OrderProduct> list, @Param("id") Integer id);
}
