package com.fh.controller;

import com.fh.model.Type;
import com.fh.service.TypeService;
import com.fh.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("TypeController")
@Controller
public class TypeController {

    @Autowired
    private TypeService typeService;


    @RequestMapping("selectType")
    @ResponseBody
    public JsonData selectType(){
        List<Type> typeList=typeService.selectType();
        return JsonData.getJsonSuccess(typeList);
    }



}
