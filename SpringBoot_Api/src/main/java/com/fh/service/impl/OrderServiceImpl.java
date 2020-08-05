package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.enums.OrderEnum;
import com.fh.common.exceptor.CountException;
import com.fh.dao.CommodityDao;
import com.fh.dao.OrderDao;
import com.fh.dao.OrderProductDao;
import com.fh.model.*;
import com.fh.service.OrderService;
import com.fh.util.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private OrderProductDao orderProductDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Map addCartByAll(Integer addressId, Integer buyId) throws CountException {

        Map map=new HashMap();
        Order order=new Order();
        order.setAddressId(addressId);
        order.setPayType(buyId);
        order.setCreateDate(new Date());
        order.setPayStatus(OrderEnum.PAY_STATUS_INIT.getStatus());
        order.setVipId(addressId);
        Integer count=0;
        BigDecimal totaMeny=new BigDecimal(0);

        Vip login_user = (Vip) request.getAttribute("login_user");

        String iphone = login_user.getIphone();

        List<OrderProduct> list=new ArrayList<>();
        //购物车中所有的数据
        List<String> productCarts = RedisUse.hvals("cart_" + iphone + "_wr");
        for (int i = 0; i <productCarts.size() ; i++) {

            Cart cart = JSONObject.parseObject(productCarts.get(i), Cart.class);
            //判断是否是订单中的商品
            if(cart.isCheck()==true){

                Commodity commodity = commodityDao.selectById(cart.getId());
                if(commodity.getStock()>=cart.getCount()){
                    count++;
                    totaMeny= totaMeny.add(cart.getMoney());

                    OrderProduct orderProduct=new OrderProduct();
                    orderProduct.setCommodityId(cart.getId());
                    orderProduct.setCount(cart.getCount());
                    list.add(orderProduct);

                    commodity.setStock(commodity.getStock()-cart.getCount());
                    commodityDao.updateById(commodity);
                    //超卖
                    int i1=commodityDao.updateCount(commodity.getId(),cart.getCount());
                    if(i1==0){//超卖
                        throw new CountException("商品为："+cart.getName()+"库存只有："+commodity.getStock()+"");
                    }
                }else{
                    //库存不够
                    throw new CountException("商品为："+cart.getName()+"库存只有："+commodity.getStock()+"");
                }
            }
        }
        order.setTotalMoney(totaMeny);
        orderDao.insert(order);
        orderProductDao.bathAdd(list,order.getId());
        for (int i = 0; i <productCarts.size() ; i++) {
            Cart cart = JSONObject.parseObject(productCarts.get(i), Cart.class);
            if(cart.isCheck()==true){
                RedisUse.hdel("cart_" + iphone + "_wr",cart.getId()+"");
            }
        }
        map.put("orderId",order.getId());
        map.put("totaMeny",totaMeny);
        return map;
    }


    @Override
    public Map createCode(Integer orderId) throws Exception {
        Map rs=new HashMap();
        String meonyPhotoUrl = RedisUse.get("order_"+orderId+"_wr");
        if(StringUtils.isEmpty(meonyPhotoUrl)!=true){//不为空  已经生成过二维码
            rs.put("code",200);
            rs.put("url",meonyPhotoUrl);
            return rs;
        }
        Order order = orderDao.selectById(orderId);
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商666-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","weixin1_order_wr_"+orderId);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分
        data.put("total_fee","1");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        //这一块必须用log4j 做记录的
        System.out.println(orderId+"下订单结果为:"+ JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            rs.put("code",200);
            rs.put("url",resp.get("code_url"));
            //更新订单状态
            order.setPayStatus(OrderEnum.PAY_STATUS_ING.getStatus());
            orderDao.updateById(order);
            //将二维码存入redis  设置失效时间
            RedisUse.set("order_"+orderId+"_wr",resp.get("code_url"),30*60);
        }else {
            rs.put("code",600);
            rs.put("info",resp.get("return_msg"));
        }
        return rs;

    }

    @Override
    public Integer selectPayStatus(Integer orderId) throws Exception {
        FeiConfig config = new FeiConfig();
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no","weixin1_order_wr_"+orderId);
        // 查询支付状态
        Map<String, String> resp = wxpay.orderQuery(data);
        System.out.println("查询结果："+JSONObject.toJSONString(resp));
        if("SUCCESS".equalsIgnoreCase(resp.get("return_code"))&&"SUCCESS".equalsIgnoreCase(resp.get("result_code"))){
            if("SUCCESS".equalsIgnoreCase(resp.get("trade_state"))){//支付成功
                //更新订单状态
                Order order=new Order();
                order.setId(orderId);
                order.setPayStatus(OrderEnum.PAY_STATUS_SUCCESS.getStatus());
                orderDao.updateById(order);
                return 1;
            }else if("NOTPAY".equalsIgnoreCase(resp.get("trade_state"))){
                return 3;
            }else if("USERPAYING".equalsIgnoreCase(resp.get("trade_state"))){
                return 2;
            }
        }
        return 0;
    }

    @Override
    public DataTableResult selectOrder(OderVo order) {
        Long count=orderDao.selectCountOder(order);
        List<Order> addressList=orderDao.selectOrder(order);
        DataTableResult result=new DataTableResult(order.getDraw(),count,count,addressList);
        return result;
    }


    @Override
    public void deleteOrder(Integer id) {
        orderDao.deleteById(id);
    }

    @Override
    public Order selectById(Integer id) {
        return orderDao.selectById(id);
    }
}
