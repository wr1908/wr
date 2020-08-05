package com.fh.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.dao.CartDao;
import com.fh.dao.CommodityDao;
import com.fh.model.Commodity;
import com.fh.model.Vip;
import com.fh.model.Cart;
import com.fh.service.CartService;
import com.fh.util.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {


    @Resource
    private CartDao cartDao;


    @Resource
    private CommodityDao commodityDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Long addCart(Integer id, Integer count) {
        if(count>0) {
            //判断库存
            Commodity commodity = commodityDao.selectById(id);

            if (count > commodity.getStock()) {
            return Long.valueOf(commodity.getStock() - count);
        }
    }

        //将数据存到redis
        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        String hget = RedisUse.hget("cart_" + iphone + "_wr", id + "");

        if(StringUtils.isEmpty(hget)){

            //查询商品信息
            Cart cart=cartDao.selectCartById(id);
            cart.setCheck(true);
            cart.setCount(count);
            //算小计 .multiply 乘积
            BigDecimal moeny=cart.getPrice().multiply(new BigDecimal(count));

            cart.setMoney(moeny);
            //将商品转换成json
            String cartString = JSONObject.toJSONString(cart);
            //将数据存到redis
            RedisUse.hset("cart_"+iphone+"_wr",id+"",cartString);

        }else {

            //将json字符串转换为java
            Cart cart = JSON.parseObject(hget, Cart.class);

            cart.setCount(cart.getCount()+count);

            //修改个数
            BigDecimal money1 = cart.getPrice().multiply(new BigDecimal(cart.getCount()));
            cart.setMoney(money1);

            //将商品信息转换为商品字符串
            String cartString = JSONObject.toJSONString(cart);
            //将数据存到redis
            RedisUse.hset("cart_"+iphone+"_wr",id+"",cartString);
        }

        //获取商品种类的个数
        Long hlen= RedisUse.hlen("cart_"+iphone+"_wr");
        return hlen;
    }

    @Override
    public List selectCartByUser() {

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_wr");
        return productCarts;
    }

    @Override
    public void deleteById(Integer id) {

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        RedisUse.hdel("cart_" + iphone + "_wr",id + "");

    }

    @Override
    public void deleteAll(Integer[] ids) {

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        for(Integer id:ids){

            RedisUse.hdel("cart_" + iphone + "_wr",id + "");

        }

    }

    @Override
    public void updateCartStatus(String ids) {

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_wr");

        for (int i = 0; i < productCarts.size(); i++) {
            String str = productCarts.get(i);

            Cart cart = JSONObject.parseObject(str, Cart.class);

            Integer id = cart.getId();

            if((","+ids).contains(","+id+",")==true){
                cart.setCheck(true);
                RedisUse.hset("cart_" + iphone + "_wr",cart.getId()+"",JSONObject.toJSONString(cart));
            }else{
                cart.setCheck(false);
                RedisUse.hset("cart_" + iphone + "_wr",cart.getId()+"",JSONObject.toJSONString(cart));
            }
        }
    }

    @Override
    public List<Cart> selectCheck() {

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        //购物车中所有的数据
        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_wr");

        //实际需要的数据
        List<Cart> cartList=new ArrayList<>();

        for (int i = 0; i < productCarts.size(); i++) {
            String str = productCarts.get(i);
            Cart cart = JSONObject.parseObject(str, Cart.class);
            if(cart.isCheck()==true){
                cartList.add(cart);
            }
        }
        return cartList;
    }


}
