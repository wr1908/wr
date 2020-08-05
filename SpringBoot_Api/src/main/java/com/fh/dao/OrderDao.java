package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.OderVo;
import com.fh.model.Order;

import java.util.List;

public interface OrderDao extends BaseMapper<Order> {
    Long selectCountOder(OderVo order);

    List<Order> selectOrder(OderVo order);
}
