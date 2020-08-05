package com.fh.controller;


import com.fh.model.Address;
import com.fh.model.DataTableResult;
import com.fh.service.AddressSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("AddressController")
@Controller
public class AddressController {

    @Autowired
    private AddressSrevice addressSrevice;


    @RequestMapping("selectAddress")
    @ResponseBody
    public DataTableResult selectAddress(Address address){
        return addressSrevice.selectAddress(address);
    }

}
