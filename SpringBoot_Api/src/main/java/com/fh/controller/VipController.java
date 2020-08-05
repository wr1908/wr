package com.fh.controller;

import com.fh.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("VipController")
@Controller
public class VipController {


    @Autowired
    private VipService vipService;




}
