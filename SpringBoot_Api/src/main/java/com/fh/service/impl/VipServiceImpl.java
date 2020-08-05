package com.fh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.dao.VipDao;
import com.fh.model.Vip;
import com.fh.service.VipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VipServiceImpl implements VipService {

    @Resource
    private VipDao vipDao;

    @Override
    public Vip selectVip(String iphone) {
        QueryWrapper<Vip> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",iphone);
        queryWrapper.or();
        queryWrapper.eq("iphone",iphone);
        Vip vip = vipDao.selectOne(queryWrapper);
        return vip;
    }
}
