package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Address;

import java.util.List;

public interface AddressDao extends BaseMapper<Address> {
    
    Long selectAddressCount();

    List<Address> selectAddress(Address address);
}
