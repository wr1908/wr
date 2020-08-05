package com.fh.service;

import com.fh.model.Cart;

import java.util.List;

public interface CartService {

    public Long addCart(Integer id, Integer count);

    List selectCartByUser();

    void deleteById(Integer id);

    void deleteAll(Integer[] ids);

    void updateCartStatus(String ids);

    List<Cart> selectCheck();
}
