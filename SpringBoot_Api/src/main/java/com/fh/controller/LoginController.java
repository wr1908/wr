package com.fh.controller;

import com.fh.util.JsonData;
import com.fh.model.Vip;
import com.fh.service.VipService;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("LoginController")
public class LoginController {

    @Autowired
    private VipService vipService;

    @RequestMapping("sendMessage")
    public JsonData sendMessage(String iphone){
        Vip vip=vipService.selectVip(iphone);
        if(vip!=null){
            String code="111";
            RedisUse.set(iphone+"_wr",code,60*5);
            return JsonData.getJsonSuccess("短信发送成功");
        }else {
            return JsonData.getJsonSuccess("用户名或手机号不存在");
        }

    }



    @RequestMapping("toLogin")
    public JsonData toLogin(String iphone,String code){
        Map logMap=new HashMap();
        String redis_code = RedisUse.get(iphone+"_wr");
        if(redis_code!=null && redis_code.equals(code)){
            Map user=new HashMap();
            user.put("iphone",iphone);
            String sign = JWT.sign(user,1000 * 60 * 60 * 24);
            //最新的密钥
            RedisUse.set("token_"+iphone,sign,60*30);
            //加签，手机号+sign 防止篡改数据
            String token = Base64.getEncoder().encodeToString((iphone + "," + sign).getBytes());
            logMap.put("status",200);
            logMap.put("message","登陆成功");
            logMap.put("token",token);
        }else {
            logMap.put("status",300);
            logMap.put("message","用户不存在或短信验证错误");
        }
        return JsonData.getJsonSuccess(logMap);
    }




}
