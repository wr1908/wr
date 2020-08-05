package com.fh.controller;

import com.fh.common.exceptor.CountException;
import com.fh.model.DataTableResult;
import com.fh.model.OderVo;
import com.fh.model.Order;
import com.fh.service.OrderService;
import com.fh.util.JsonData;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("OrderController")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping("addCartByAll")
    @ResponseBody
    public JsonData addCartByAll(Integer addressId,Integer buyId,String flag)throws CountException {
        boolean exists = RedisUse.exists(flag);
        if(exists==true){
            return JsonData.getJsonError(300,"请求处理中");
        }else{
            RedisUse.set(flag,"",10);
        }
        Map map =orderService.addCartByAll(addressId,buyId);
        return JsonData.getJsonSuccess(map);
    }

    @RequestMapping("createCode")//提供访问路径
    @ResponseBody
    public JsonData createCode(Integer orderId) throws Exception {
        Map meonyPhoto = orderService.createCode(orderId);
        return JsonData.getJsonSuccess(meonyPhoto);
    }



    @RequestMapping("queryPayStatus")
    @ResponseBody
    public JsonData queryPayStatus(Integer orderId) throws Exception {
        Integer status=orderService.selectPayStatus(orderId);
        return JsonData.getJsonSuccess(status);
    }


    @RequestMapping("selectOrder")
    @ResponseBody
    public DataTableResult selectOrder(OderVo order){
        return orderService.selectOrder(order);
    }

    @RequestMapping("deleteOrder")
    @ResponseBody
    public JsonData deleteOrder(Integer id){
        orderService.deleteOrder(id);
        return JsonData.getJsonSuccess(id);
    }


    @RequestMapping("selectById")
    @ResponseBody
    public Order selectById(Integer id){
        return orderService.selectById(id);
    }

}
