package com.fh.service.impl;

import com.fh.dao.AddressDao;
import com.fh.dao.VipDao;
import com.fh.model.Address;
import com.fh.model.DataTableResult;
import com.fh.service.AddressSrevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressSrevice {

    @Resource
    private AddressDao addressDao;

    @Resource
    private VipDao vipDao;

    @Override
    public DataTableResult selectAddress(Address address) {
        Long count=addressDao.selectAddressCount();
        List<Address> addressList=addressDao.selectAddress(address);
        DataTableResult result=new DataTableResult(address.getDraw(),count,count,addressList);
        //处理地区
        for (int i = 0; i < addressList.size(); i++) {
            //获取一个类型的信息
            String areaId=addressList.get(i).getAreaIds();
            //判断不为空
            if(!StringUtils.isEmpty(areaId)){
                List<String> strings=vipDao.selectNameById(areaId);
                StringBuffer sb=new StringBuffer();
                for (int j = 0; j < strings.size(); j++) {
                    sb.append(strings.get(j)).append(" ");
                }
                addressList.get(i).setAreaIds(sb.toString());
            }
        }
        return result;
    }
}
