package com.fh;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("TestController")
@RestController
public class TestController {

    @RequestMapping("test")
    public String test(){
        System.out.println("hello world");
        return "success";
    }
}
