package com.fh.common.interceptor;

import com.fh.common.exceptor.NologinException;
import com.fh.model.Vip;
import com.fh.util.JWT;
import com.fh.util.JsonData;
import com.fh.util.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从header中获取token
       String token=request.getHeader("token");
       
       //验证头信息是否完整
        if(StringUtils.isEmpty(token)){
            throw new Exception("头部信息不完整");
        }

        //解密
        byte[] decode = Base64.getDecoder().decode(token);

        //得到字符串 字节数组转为字符串 iphone+","+sign
        String signToken=new String(decode);

        //判断是否被篡改
        String[] split = signToken.split(",");

        if(split.length!=2){
            throw new Exception("token格式不正确");
        }

        String iphone=split[0];

        String sign=split[1];

        Vip user= JWT.unsign(sign,Vip.class);
        if(user==null){
            System.out.println("没有登陆");

            throw new NologinException("没有登录");
        }else{
            JsonData.getJsonSuccess("登录成功");
        }


        if(user!=null){
            String sign_redis = RedisUse.get("token_" + user.getIphone());
            if(!sign.equals(sign_redis)){
                throw new NologinException("验证过期 请重新登陆");
            }
        }


        //前面逻辑验证过了  设置redis key值的有效时间 为30
        RedisUse.set("token_"+user.getIphone(),sign,60*30);
        request.setAttribute("login_user",user);
        return true;
    }

}
