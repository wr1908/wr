package com.fh.service.impl;

import com.fh.dao.TypeDao;
import com.fh.model.Type;
import com.fh.service.TypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {


    @Resource
    private TypeDao typeDao;

    @Override
    public List<Type> selectType() {
        return typeDao.selectList(null);
    }
}
