package com.fh.controller;

import com.fh.util.JsonData;
import com.fh.model.Cart;
import com.fh.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("CartController")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("addCart")
    public JsonData addCart(Integer id,Integer count){
        Map map=new HashMap();
        Long count_type = cartService.addCart(id, count);
        map.put("count_type",count_type);
        map.put("id",id);
        return JsonData.getJsonSuccess(map);
    }


    @RequestMapping("queryCartByUser")
    public JsonData queryCartListByUser(){
        List cartList=cartService.selectCartByUser();
        return JsonData.getJsonSuccess(cartList);
    }


    @RequestMapping("deleteById")
    public JsonData deleteById(Integer id){
        cartService.deleteById(id);
        return JsonData.getJsonSuccess(id);
    }



    @RequestMapping("deleteAll")
    public JsonData deleteAll(Integer[] ids){
        cartService.deleteAll(ids);
        return JsonData.getJsonSuccess(ids);
    }


    @RequestMapping("updateCartStatus")
    public JsonData updateCartStatus(String ids){
        cartService.updateCartStatus(ids);
        return JsonData.getJsonSuccess(ids);
    }



    @RequestMapping("selectCheck")
    public JsonData selectCheck(){
        List<Cart> cartList=cartService.selectCheck();
        return JsonData.getJsonSuccess(cartList);
    }
}
