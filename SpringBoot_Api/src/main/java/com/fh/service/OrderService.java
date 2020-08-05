package com.fh.service;

import com.fh.common.exceptor.CountException;
import com.fh.model.DataTableResult;
import com.fh.model.OderVo;
import com.fh.model.Order;

import java.util.Map;

public interface OrderService {

    Map addCartByAll(Integer addressId, Integer buyId) throws CountException;

    Map createCode(Integer orderId) throws Exception;

    Integer selectPayStatus(Integer orderId) throws Exception;

    DataTableResult selectOrder(OderVo order);

    void deleteOrder(Integer id);

    Order selectById(Integer id);
}
