package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.Cart;

public interface CartDao extends BaseMapper<Cart> {

    Cart selectCartById(Integer id);

    void deleteAll(Integer[] ids);
}
